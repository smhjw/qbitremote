package com.hjw.qbremote.data

import com.hjw.qbremote.data.model.DashboardData
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class QbRepository {
    private var api: QbApi? = null

    suspend fun connect(settings: ConnectionSettings): Result<Unit> = runCatching {
        require(settings.host.isNotBlank()) { "Host cannot be empty." }
        require(settings.port in 1..65535) { "Port must be between 1 and 65535." }

        val newApi = buildApi(settings.baseUrl())
        val loginResponse = newApi.login(settings.username, settings.password)
        val loginText = loginResponse.body()?.trim().orEmpty()
        val loginOk = loginResponse.isSuccessful && loginText.equals("Ok.", ignoreCase = true)
        if (!loginOk) {
            throw IllegalStateException("Login failed. Check host, credentials, and WebUI settings.")
        }

        api = newApi
    }

    suspend fun fetchDashboard(): Result<DashboardData> = runCatching {
        val liveApi = requireApi()
        val transfer = liveApi.transferInfo()
        val torrents = liveApi.torrentsInfo().sortedByDescending { it.addedOn }
        DashboardData(transferInfo = transfer, torrents = torrents)
    }

    suspend fun pauseTorrent(hash: String): Result<Unit> = runCatching {
        require(hash.isNotBlank()) { "Invalid torrent hash." }
        requireApi().pauseTorrents(hash).ensureSuccess("Pause failed.")
    }

    suspend fun resumeTorrent(hash: String): Result<Unit> = runCatching {
        require(hash.isNotBlank()) { "Invalid torrent hash." }
        requireApi().resumeTorrents(hash).ensureSuccess("Resume failed.")
    }

    suspend fun deleteTorrent(hash: String, deleteFiles: Boolean): Result<Unit> = runCatching {
        require(hash.isNotBlank()) { "Invalid torrent hash." }
        requireApi().deleteTorrents(hash, deleteFiles).ensureSuccess("Delete failed.")
    }

    private fun buildApi(baseUrl: String): QbApi {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .cookieJar(SessionCookieJar())
            .addInterceptor(logger)
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QbApi::class.java)
    }

    private fun requireApi(): QbApi {
        return api ?: throw IllegalStateException("Not connected to qBittorrent yet.")
    }

    private fun Response<*>.ensureSuccess(defaultMessage: String) {
        if (!isSuccessful) {
            val extra = errorBody()?.string()?.takeIf { it.isNotBlank() } ?: code().toString()
            throw IllegalStateException("$defaultMessage ($extra)")
        }
    }

    private class SessionCookieJar : CookieJar {
        private val cookieStore = mutableMapOf<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host].orEmpty()
        }
    }
}

package com.hjw.qbremote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hjw.qbremote.data.ConnectionStore
import com.hjw.qbremote.data.QbRepository
import com.hjw.qbremote.ui.MainScreen
import com.hjw.qbremote.ui.MainViewModel
import com.hjw.qbremote.ui.theme.QBRemoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val connectionStore = ConnectionStore(applicationContext)
        val repository = QbRepository()

        setContent {
            QBRemoteTheme {
                val vm: MainViewModel = viewModel(
                    factory = MainViewModel.factory(connectionStore, repository)
                )
                MainScreen(viewModel = vm)
            }
        }
    }
}

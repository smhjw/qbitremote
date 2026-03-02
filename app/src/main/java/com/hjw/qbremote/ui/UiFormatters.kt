package com.hjw.qbremote.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatBytes(value: Long): String {
    if (value <= 0L) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var size = value.toDouble()
    var idx = 0
    while (size >= 1024 && idx < units.lastIndex) {
        size /= 1024.0
        idx++
    }
    return String.format(Locale.US, "%.2f %s", size, units[idx])
}

fun formatSpeed(value: Long): String = "${formatBytes(value)}/s"

fun formatPercent(progress: Float): String {
    val pct = (progress * 100f).coerceIn(0f, 100f)
    return String.format(Locale.US, "%.2f%%", pct)
}

fun formatAddedOn(seconds: Long): String {
    if (seconds <= 0L) return "-"
    val date = Date(seconds * 1000L)
    return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date)
}

fun stateLabel(state: String): String {
    return when (state.lowercase(Locale.getDefault())) {
        "downloading", "stalleddl", "forceddl", "metadl" -> "Downloading"
        "uploading", "stalledup", "forcedup" -> "Seeding"
        "pauseddl", "pausedup" -> "Paused"
        "error", "missingfiles" -> "Error"
        "queueddl", "queuedup" -> "Queued"
        else -> state
    }
}

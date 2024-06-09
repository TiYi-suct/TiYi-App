package com.tiyi.tiyi_app.data

data class MusicInfo(
    val id: Int,
    val title: String,
    val description: String,
    val tags: List<String> = emptyList(),
)
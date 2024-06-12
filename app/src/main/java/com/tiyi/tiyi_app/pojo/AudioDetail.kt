package com.tiyi.tiyi_app.pojo

data class AudioDetail(
    val audioId: String,
    val name: String,
    val extension: String,
    val url: String,
    val tags: List<String>,
    val cover: String,
    val description: String,
    val userName: String
)

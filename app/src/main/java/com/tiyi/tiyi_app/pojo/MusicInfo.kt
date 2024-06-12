package com.tiyi.tiyi_app.pojo

data class MusicInfo(
    val id: String,
    val title: String,
    val tags: List<String> = emptyList(),
) {
    val tagList: String
        get() = if (tags.isNotEmpty())
            tags.joinToString(", ")
        else "无标签"
}
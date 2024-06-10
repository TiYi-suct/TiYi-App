package com.tiyi.tiyi_app.data

data class MusicInfo(
    val id: Int,
    val title: String,
    val tags: List<String> = emptyList(),
) {
    val description: String
        get() = if (tags.isNotEmpty())
            tags.joinToString(", ")
        else "无标签"
}
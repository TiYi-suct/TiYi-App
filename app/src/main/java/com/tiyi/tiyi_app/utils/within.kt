package com.tiyi.tiyi_app.utils

import kotlinx.coroutines.flow.MutableStateFlow

suspend fun MutableStateFlow<Boolean>.within(block: suspend () -> Unit) = try {
    value = true
    block()
} finally {
    value = false
}
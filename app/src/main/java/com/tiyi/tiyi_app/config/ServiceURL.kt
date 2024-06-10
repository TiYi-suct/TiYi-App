package com.tiyi.tiyi_app.config

import android.content.Context
import java.util.Properties

fun isFileExists(context: Context, fileName: String): Boolean {
    return try {
        context.assets.open(fileName).close()
        true
    } catch (e: Exception) {
        false
    }
}

fun getServiceUrl(context: Context): String {
    val propertyFile = if (isFileExists(context, "config-debug.properties")) {
        "config-debug.properties"
    } else {
        "config.properties"
    }
    val properties = Properties()
    context.assets.open(propertyFile).use { properties.load(it) }
    return properties.getProperty("service_url")
}
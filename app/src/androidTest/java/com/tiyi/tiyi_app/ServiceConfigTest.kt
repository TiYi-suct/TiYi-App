package com.tiyi.tiyi_app

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tiyi.tiyi_app.config.getServiceUrl
import com.tiyi.tiyi_app.config.isFileExists
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ServiceConfigTest {
    @Test
    fun bothPropertiesExists() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assert(isFileExists(appContext, "config.properties"))
        assert(isFileExists(appContext, "config-debug.properties"))
        val serviceUrl = getServiceUrl(appContext)
        // 这个serviceUrl应该会返回你自己的config-debug.properties文件下的URL地址
//        assert(serviceUrl == "http://192.168.137.1:5000")
    }
}
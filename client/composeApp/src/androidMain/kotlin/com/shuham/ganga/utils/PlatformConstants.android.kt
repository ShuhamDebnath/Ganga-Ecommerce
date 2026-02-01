package com.shuham.ganga.utils

actual object PlatformConstants {
    // Android Emulator needs 10.0.2.2 to reach the host
    actual val BASE_URL: String = "http://10.0.2.2:8000/api/v1/"
}
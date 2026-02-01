package com.shuham.ganga.utils

actual object PlatformConstants {
    // iOS Simulator uses localhost to reach the host
    actual val BASE_URL: String = "http://localhost:8000/api/v1/"
}
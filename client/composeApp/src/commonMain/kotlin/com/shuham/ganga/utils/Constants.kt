package com.shuham.ganga.utils

object Constants {
    // 10.0.2.2 is the special alias to host loopback interface (127.0.0.1)
    // on the development machine from the Android emulator.
    // For Desktop, we use localhost.
    const val BASE_URL_ANDROID = "http://10.0.2.2:8000/api/v1/"
    const val BASE_URL_DESKTOP = "http://localhost:8000/api/v1/"
}
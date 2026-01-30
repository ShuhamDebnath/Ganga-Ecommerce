package com.shuham.ganga

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
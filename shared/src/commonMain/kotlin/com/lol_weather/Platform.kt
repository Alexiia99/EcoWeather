package com.lol_weather

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
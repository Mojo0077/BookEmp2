package com.example.bookemp.models

import java.util.*

data class Book(
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var createdAt: Date? = null,
    var year: Int = 0,
    val coverImageUri: String = "" // ⬅️ új mező
)




package com.example.bookemp.models

import com.google.firebase.Timestamp
import java.util.Date

data class Library(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val createdAt: Date = Date()
)

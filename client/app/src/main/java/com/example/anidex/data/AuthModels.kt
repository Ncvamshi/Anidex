package com.example.anidex.data

import com.google.gson.annotations.SerializedName

// For Login & Register inputs
data class AuthRequest(
    val username: String,
    val password: String
)

// For the response: { "message": "...", "user_id": "..." }
data class AuthResponse(
    val message: String,
    @SerializedName("user_id") val userId: String
)
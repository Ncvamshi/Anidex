package com.example.anidex.data

import com.google.gson.annotations.SerializedName

// --- Wrapper for Collection List Items ---
data class CollectionResponse(
    val id: Int,
    val confidence: Float,
    @SerializedName("scanned_at") val scannedAt: String,
    val animal: Animal
)

// --- Wrapper for Single Prediction ---
data class PredictResponse(
    @SerializedName("already_collected") val alreadyCollected: Boolean,
    @SerializedName("scan_id") val scanId: Int,
    val confidence: Float,
    @SerializedName("vision_label") val visionLabel: String,
    val animal: Animal
)

// --- The Base Animal Model ---
data class Animal(
    val id: Int? = null,
    val name: String = "Unknown",
    @SerializedName("common_name") val commonName: String? = null,
    @SerializedName("scientific_name") val scientificName: String? = null,
    val category: String? = null,
    val description: String? = null,
    @SerializedName("max_height") val maxHeight: String? = null,
    @SerializedName("max_weight") val maxWeight: String? = null,
    val diet: String? = null,
    val habitat: String? = null,
    val colors: List<String>? = emptyList(),
    val lifestyle: String? = null,
    @SerializedName("fun_fact") val funFact: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    var localUri: String? = null
)
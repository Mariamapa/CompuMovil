package com.example.dragonki.data.model

import com.google.gson.annotations.SerializedName

data class CharacterSummary(
    val id: Int,
    val name: String,
    val affiliation: String?,
    @SerializedName("image") val imageUrl: String?
)

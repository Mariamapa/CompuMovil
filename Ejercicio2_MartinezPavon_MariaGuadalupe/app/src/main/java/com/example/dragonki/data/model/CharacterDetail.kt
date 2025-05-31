package com.example.dragonki.data.model

import com.google.gson.annotations.SerializedName

data class Transformation(
    val name: String,
    @SerializedName("image") val imageUrl: String,
    val ki: String?
)

data class CharacterDetail(
    val id: Int,
    val name: String,
    val affiliation: String?,
    val ki: String?,
    @SerializedName("maxKi") val maxKi: String?,
    val race: String?,
    val gender: String?,
    val description: String?,
    @SerializedName("image") val imageUrl: String?,
    val transformations: List<Transformation>?
)

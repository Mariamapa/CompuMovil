package com.example.dragonki.data.model

import com.google.gson.annotations.SerializedName

data class CharacterListResponse(
    @SerializedName("items") val characters: List<CharacterSummary>
)



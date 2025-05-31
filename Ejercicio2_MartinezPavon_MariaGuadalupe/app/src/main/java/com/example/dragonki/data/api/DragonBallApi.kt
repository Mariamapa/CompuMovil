package com.example.dragonki.data.api

import com.example.dragonki.data.model.CharacterDetail
import com.example.dragonki.data.model.CharacterListResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DragonBallApi {

    @GET("api/characters")
    suspend fun listCharacters(@Query("limit") limit: Int): CharacterListResponse

    @GET("api/characters/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDetail

    @GET("api/characters")
    suspend fun getCharactersRaw(@Query("limit") limit: Int): Response<ResponseBody>
}
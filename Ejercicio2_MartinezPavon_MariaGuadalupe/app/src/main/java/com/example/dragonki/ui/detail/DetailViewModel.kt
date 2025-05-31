package com.example.dragonki.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonki.data.api.ApiClient
import com.example.dragonki.data.model.CharacterDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _character = MutableStateFlow<CharacterDetail?>(null)
    val character: StateFlow<CharacterDetail?> = _character

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun load(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("DetailViewModel", "Solicitando personaje con ID: $id")
                val detail = ApiClient.api.getCharacter(id)
                _character.value = detail
                Log.d("DetailViewModel", "Detalle recibido: $detail")
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error cargando personaje con ID $id", e)
                _character.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}

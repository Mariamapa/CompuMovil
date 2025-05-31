package com.example.dragonki.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonki.data.api.ApiClient
import com.example.dragonki.data.model.CharacterSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<CharacterSummary>>(emptyList())
    val characters: StateFlow<List<CharacterSummary>> = _characters

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                val response = ApiClient.api.listCharacters(limit = 58)
                _characters.value = response.characters
                //Logcat para debuging
                Log.d("ListViewModel", "Cargados ${response.characters.size} personajes")
                Log.d("ListViewModel", "Primer personaje: ${response.characters.firstOrNull()?.name}, Imagen: ${response.characters.firstOrNull()?.imageUrl}")
            } catch (e: Exception) {
                Log.e("ListViewModel", "Error cargando personajes", e)
                _characters.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

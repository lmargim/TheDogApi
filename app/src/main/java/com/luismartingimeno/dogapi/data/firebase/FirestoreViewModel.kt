package com.luismartingimeno.dogapi.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.luismartingimeno.dogapi.data.firebase.FirestoreManager
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import kotlinx.coroutines.launch

class FirestoreViewModel(
    private val firestoreManager: FirestoreManager
) : ViewModel() {

    // LiveData para los favoritos
    private val _favorites = MutableLiveData<List<DogBreedItem>>()
    val favorites: LiveData<List<DogBreedItem>> get() = _favorites

    // LiveData para el estado de sincronización
    private val _syncState = MutableLiveData<SyncState>()
    val syncState: LiveData<SyncState> get() = _syncState

    // LiveData para el estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Estado de sincronización
    sealed class SyncState {
        object Loading : SyncState()
        data class Success(val message: String) : SyncState()
        data class Error(val exception: Throwable) : SyncState()
    }

    // Agregar un favorito
    fun addFavorite(dog: DogBreedItem) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                firestoreManager.addFavorite(dog)
                _syncState.value = SyncState.Success("Favorito agregado")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }

    // Obtener la lista de favoritos
    fun getFavorites() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val favorites = firestoreManager.getFavorites()
                _favorites.value = favorites
                _syncState.value = SyncState.Success("Favoritos cargados")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Eliminar un favorito
    fun removeFavorite(breedId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                firestoreManager.removeFavorite(breedId)
                _syncState.value = SyncState.Success("Favorito eliminado")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }

    // Agregar un favorito personalizado
    fun addCustomFavorite(dog: DogBreedItem) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                firestoreManager.addCustomFavorite(dog)
                _syncState.value = SyncState.Success("Favorito personalizado agregado")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }

    // Actualizar un favorito
    fun updateFavorite(updatedBreed: DogBreedItem) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                firestoreManager.updateFavorite(updatedBreed)
                getFavorites() // Recargar la lista de favoritos después de la actualización
                _syncState.value = SyncState.Success("Favorito actualizado")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Factory para crear el ViewModel
    class FirestoreViewModelFactory(
        private val firestoreManager: FirestoreManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FirestoreViewModel(firestoreManager) as T
        }
    }
}
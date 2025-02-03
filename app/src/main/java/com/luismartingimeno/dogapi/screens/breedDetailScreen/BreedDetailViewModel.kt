package com.luismartingimeno.dogapi.screens.breedDetailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luismartingimeno.dogapi.data.model.DogBreedItem
import com.luismartingimeno.dogapi.data.repositories.RemoteConnection.remoteService
import kotlinx.coroutines.launch

class BreedDetailViewModel() : ViewModel() {

    private val _breedDetail = MutableLiveData<DogBreedItem?>()
    val breedDetail: LiveData<DogBreedItem?> get() = _breedDetail

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun getBreedDetailById(breedId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = remoteService.getBreeds()
                if (response.isSuccessful) {
                    val breed = response.body()?.find { it.id == breedId }
                    _breedDetail.value = breed
                } else {
                    _error.value = "Error al cargar los datos"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
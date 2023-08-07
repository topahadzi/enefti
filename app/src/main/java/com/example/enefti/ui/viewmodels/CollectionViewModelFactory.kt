package com.example.enefti.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.enefti.data.repository.OpenSeaRepository

class CollectionViewModelFactory constructor(private val repository: OpenSeaRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CollectionViewModel::class.java)) {
            CollectionViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
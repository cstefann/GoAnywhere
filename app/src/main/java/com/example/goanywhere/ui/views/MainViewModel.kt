package com.example.goanywhere.ui.views

import androidx.lifecycle.ViewModel
import com.example.goanywhere.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
}
package com.sceballosdev.quikstream.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.domain.usecase.GetStreamsUseCase
import com.sceballosdev.quikstream.ui.main.MainUiState.Error
import com.sceballosdev.quikstream.ui.main.MainUiState.Idle
import com.sceballosdev.quikstream.ui.main.MainUiState.Loading
import com.sceballosdev.quikstream.ui.main.MainUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getStreamsUseCase: GetStreamsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(value = Idle)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun fetchStreams() {
        viewModelScope.launch {
            getStreamsUseCase().collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> Loading
                    is Result.Success -> Success(streams = result.data)
                    is Result.Error -> Error(message = result.exception.message ?: "Unknown Error getting the videos")
                }
            }
        }
    }
}

sealed interface MainUiState {
    data object Idle : MainUiState
    data object Loading : MainUiState
    data class Success(val streams: List<Stream>) : MainUiState
    data class Error(val message: String) : MainUiState
}
package com.example.encryptionkurs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.encryptionkurs.data.fromBase64
import com.example.encryptionkurs.domain.EncryptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EncryptionViewModel @Inject constructor(
    private val encryptionRepository: EncryptionRepository,
) : ViewModel() {

    private var _outputDataState: MutableStateFlow<String> = MutableStateFlow("")
    val outputDataState: StateFlow<String> = _outputDataState

    private var _currentUrlState: MutableStateFlow<String> = MutableStateFlow("")
    val currentUrlState: StateFlow<String> = _currentUrlState


    init {
        viewModelScope.launch {
            _currentUrlState.value = encryptionRepository.getCurrentUrl()
        }
    }

    fun proceedData(dataToEncrypt: String, algorithm: Int, operation: String) {
        viewModelScope.launch {
            encryptionRepository.proceedData(
                dataToEncrypt, algorithm, operation
            ).let { result ->
                val data = result.getOrNull()

                if (result.isSuccess && data != null) {
                    _outputDataState.value = data.bytes.fromBase64()?.toString(Charsets.UTF_8) ?: ""
                } else {
                    _outputDataState.value = result.exceptionOrNull()?.message ?: ""
                }
            }
        }

    }

    fun saveUrl(url: String) {
        viewModelScope.launch {
            encryptionRepository.changeCurrentUrl(url)
        }
    }

}
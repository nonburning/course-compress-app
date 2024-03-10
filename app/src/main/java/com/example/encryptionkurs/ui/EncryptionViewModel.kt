package com.example.encryptionkurs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun proceedData(dataToEncrypt: String, algorithm: Int, operation: String) {
        viewModelScope.launch {
            encryptionRepository.proceedData(
                dataToEncrypt, algorithm, operation
            ).let { result ->
                val data = result.getOrNull()

                if (result.isSuccess && data != null) {
                    _outputDataState.value = data.bytes
                } else {
                    _outputDataState.value = result.exceptionOrNull()?.message ?: ""

                }
            }
        }

    }

}
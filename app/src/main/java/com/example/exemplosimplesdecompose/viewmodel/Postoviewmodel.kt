package com.example.exemplosimplesdecompose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.exemplosimplesdecompose.data.Posto
import com.example.exemplosimplesdecompose.data.PostoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostoRepository(application)

    private val _postos = MutableStateFlow<List<Posto>>(emptyList())
    val postos: StateFlow<List<Posto>> = _postos.asStateFlow()

    private val _alcoolSelecionado = MutableStateFlow(repository.getSwitchState())
    val alcoolSelecionado: StateFlow<Boolean> = _alcoolSelecionado.asStateFlow()

    init {
        loadPostos()
    }

    private fun loadPostos() {
        _postos.value = repository.getPostos()
    }

    fun setSwitchState(value: Boolean) {
        _alcoolSelecionado.value = value
        repository.saveSwitchState(value)
    }

    fun salvarPosto(posto: Posto) {
        repository.savePosto(posto)
        loadPostos()
    }

    fun deletarPosto(id: String) {
        repository.deletePosto(id)
        loadPostos()
    }

    fun getPostoById(id: String): Posto? = _postos.value.find { it.id == id }
}
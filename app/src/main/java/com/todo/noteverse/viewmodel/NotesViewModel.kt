package com.todo.noteverse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.noteverse.datamodel.NotesDataModel
import com.todo.noteverse.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val repository: NotesRepository): ViewModel() {
    fun insert(note: NotesDataModel) = viewModelScope.launch {
        repository.insert(note)
    }
    fun delete(note: NotesDataModel) = viewModelScope.launch {
        repository.delete(note)
    }
    fun update(note: NotesDataModel) = viewModelScope.launch {
        repository.update(note)
    }
    fun getAllNotes() = repository.getAllNotes()
    fun searchNotes(input: String) = repository.searchNotes(input)
}
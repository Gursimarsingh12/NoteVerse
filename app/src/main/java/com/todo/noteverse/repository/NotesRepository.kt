package com.todo.noteverse.repository

import com.todo.noteverse.dao.NotesDao
import com.todo.noteverse.datamodel.NotesDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotesRepository @Inject constructor(private val dao: NotesDao) {
    suspend fun insert(note: NotesDataModel) = withContext(Dispatchers.IO){
        dao.insertNote(note)
    }
    suspend fun delete(note: NotesDataModel) = withContext(Dispatchers.IO){
        dao.deleteNote(note)
    }
    suspend fun update(note: NotesDataModel) = withContext(Dispatchers.IO){
        dao.updateNote(note)
    }
    fun getAllNotes() = dao.getAllNotes()
    fun searchNotes(input: String) = dao.searchNotes(input)
}
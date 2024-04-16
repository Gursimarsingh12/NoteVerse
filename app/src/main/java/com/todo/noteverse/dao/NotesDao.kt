package com.todo.noteverse.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.todo.noteverse.datamodel.NotesDataModel

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotesDataModel)
    @Delete
    suspend fun deleteNote(note: NotesDataModel)
    @Update
    suspend fun updateNote(note: NotesDataModel)
    @Query("select * from notes_table order by id desc")
    fun getAllNotes(): LiveData<List<NotesDataModel>>
    @Query("select * from notes_table where title like :input or description like :input")
    fun searchNotes(input: String): LiveData<List<NotesDataModel>>
}
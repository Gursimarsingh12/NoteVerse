package com.todo.noteverse.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.todo.noteverse.dao.NotesDao
import com.todo.noteverse.datamodel.NotesDataModel

@Database(entities = [NotesDataModel::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
}
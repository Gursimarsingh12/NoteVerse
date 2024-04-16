package com.todo.noteverse.datamodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes_table")
@Parcelize
data class NotesDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String
) : Parcelable

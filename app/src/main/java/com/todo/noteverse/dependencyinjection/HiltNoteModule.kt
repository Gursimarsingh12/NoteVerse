package com.todo.noteverse.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.todo.noteverse.adapter.NotesAdapter
import com.todo.noteverse.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltNoteModule {
    @Singleton
    @Provides
    fun createDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        NotesDatabase::class.java,
        "your_db_name"
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    @Singleton
    @Provides
    fun createDao(db: NotesDatabase) = db.getNotesDao()
}

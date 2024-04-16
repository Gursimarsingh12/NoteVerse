package com.todo.noteverse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.todo.noteverse.dao.NotesDao
import com.todo.noteverse.database.NotesDatabase
import com.todo.noteverse.databinding.ActivityMainBinding
import com.todo.noteverse.repository.NotesRepository
import com.todo.noteverse.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // Inject only database repo and dao
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var notesViewModel: NotesViewModel
    @Inject
    lateinit var notesRepository: NotesRepository
    @Inject
    lateinit var notesDao: NotesDao
    @Inject
    lateinit var notesDatabase: NotesDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpViewModel()
    }
    // ViewModel Setup so that data can be brought up to this activity
    private fun setUpViewModel(){
        notesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
    }
    // onBackPressed() helps in preventing app crashes and it follows nav_graph to go from one fragment to another
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }
}
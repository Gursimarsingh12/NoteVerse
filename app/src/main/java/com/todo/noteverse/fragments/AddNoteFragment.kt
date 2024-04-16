package com.todo.noteverse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.todo.noteverse.MainActivity
import com.todo.noteverse.R
import com.todo.noteverse.adapter.NotesAdapter
import com.todo.noteverse.databinding.FragmentAddNoteBinding
import com.todo.noteverse.datamodel.NotesDataModel
import com.todo.noteverse.repository.NotesRepository
import com.todo.noteverse.viewmodel.NotesViewModel
import javax.inject.Inject


class AddNoteFragment : Fragment() {
    private var binding: FragmentAddNoteBinding ?= null
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    @Inject
    lateinit var notesRepository: NotesRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = (activity as MainActivity).notesViewModel
        binding?.saveNoteBtn?.setOnClickListener {
            saveNotes()
        }
    }
    // helps to save note
    private fun saveNotes() {
        val noteTitle = binding?.addNoteTitle?.text.toString()
        val noteDescription = binding?.addNoteDesc?.text.toString()
        if(noteTitle.isNotEmpty()){
            val note = NotesDataModel(0, noteTitle, noteDescription)
            notesViewModel.insert(note)
            Toast.makeText(context, "Note Saved!", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.navigateUp()
        }else {
            Toast.makeText(context, "Please fill title", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
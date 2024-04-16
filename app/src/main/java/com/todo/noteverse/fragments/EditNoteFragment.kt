package com.todo.noteverse.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import com.todo.noteverse.MainActivity
import com.todo.noteverse.R
import com.todo.noteverse.databinding.FragmentEditNoteBinding
import com.todo.noteverse.datamodel.NotesDataModel
import com.todo.noteverse.repository.NotesRepository
import com.todo.noteverse.viewmodel.NotesViewModel
import javax.inject.Inject


class EditNoteFragment : Fragment() {
    private var binding: FragmentEditNoteBinding ?= null
    private lateinit var notesViewModel: NotesViewModel
    @Inject
    lateinit var notesRepository: NotesRepository
    private var noteId: Int = 0
    private var noteTitle: String = ""
    private var noteDescription: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setting up this fragment to Main activity
        notesViewModel = (activity as MainActivity).notesViewModel
        // getting same information of the note from Homefragment.kt file
        noteId = requireArguments().getInt("noteId")
        noteTitle = requireArguments().getString("noteTitle", "")
        noteDescription = requireArguments().getString("noteDescription", "")
        binding?.addNoteTitle?.text = noteTitle.toEditable()
        binding?.addNoteDesc?.text = noteDescription.toEditable()
        updateNote()
        deleteNote()
    }

    private fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }
    // update note
    private fun updateNote(){
        binding?.saveNoteBtn?.setOnClickListener {
            if(noteTitle.isNotEmpty()){
                val title = binding!!.addNoteTitle.text.toString()
                val description = binding!!.addNoteDesc.text.toString()
                val note = NotesDataModel(noteId, title, description)
                notesViewModel.update(note)
                Toast.makeText(context, "Note Saved!", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.navigateUp()
            }else {
                Toast.makeText(context, "Please fill title", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun deleteNote(){
        binding?.deleteNoteBtn?.setOnClickListener {
            PopupDialog.getInstance(context)
                .setStyle(Styles.IOS)
                .setHeading("Delete")
                .setDescription(
                    "Are you sure you want to delete this note?" +
                            " This action cannot be undone"
                )
                .setCancelable(false)
                .setPositiveButtonText("Yes")
                .setNegativeButtonText("No")
                .showDialog(object : OnDialogButtonClickListener() {
                    override fun onPositiveClicked(dialog: Dialog) {
                        super.onPositiveClicked(dialog)
                        val title = binding!!.addNoteTitle.text.toString()
                        val description = binding!!.addNoteDesc.text.toString()
                        val note = NotesDataModel(noteId, title, description)
                        notesViewModel.delete(note)
                        Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(R.id.homeFragment)
                    }
                    override fun onNegativeClicked(dialog: Dialog) {
                        super.onNegativeClicked(dialog)
                        dialog.dismiss()
                    }
                })
        }
    }
}
package com.todo.noteverse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.todo.noteverse.MainActivity
import com.todo.noteverse.R
import com.todo.noteverse.adapter.NotesAdapter
import com.todo.noteverse.databinding.FragmentHomeBinding
import com.todo.noteverse.datamodel.NotesDataModel
import com.todo.noteverse.repository.NotesRepository
import com.todo.noteverse.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var binding: FragmentHomeBinding ?= null
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    @Inject
    lateinit var notesRepository: NotesRepository
    // to store current Notes list
    private lateinit var allNotes: List<NotesDataModel>
    // Helps to use swipe actions on recycler view
    private var itemTouchHelper: ItemTouchHelper ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = (activity as MainActivity).notesViewModel
        setUpRecyclerView()
        setUpSearchView()
        binding?.addNoteBtn?.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
        swipeFeatures()
        getAllNotes()
    }
    // helps in setting up recycler view by notesAdapter, first make notesAdapter instance
    private fun setUpRecyclerView(){
        notesAdapter = context?.let { NotesAdapter(it) }!!
        binding?.recyclerView?.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
            setHasFixedSize(true)
        }
    }
    // Helps to update UI like if note is not empty then show notes wlse show empty notes image
    private fun updateUI(note: List<NotesDataModel>){
        if(note.isNotEmpty()){
            binding?.emptyNotesImg?.visibility = View.GONE
            binding?.recyclerView?.visibility = View.VISIBLE
        }else{
            binding?.emptyNotesImg?.visibility = View.VISIBLE
            binding?.recyclerView?.visibility = View.GONE
        }
    }
    // Helps in getting notes from viewModel
    private fun getAllNotes() {
        notesViewModel.getAllNotes().observe(viewLifecycleOwner) { notes ->
            // allNotes helps to store all the current Notes
            allNotes = notes
            // this helps in sending notes to adapter to set in recyclerView
            notesAdapter.differ.submitList(notes)
            updateUI(notes)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    // Helps in searching notes
    private fun filterNotes(query: String) {
        // the purpose of using double % symbols is to make the search query more flexible by
        // allowing matches to occur anywhere within the text being searched, not just at the beginning or end.
        val searchQuery = "%$query%"
        notesViewModel.searchNotes(searchQuery).observe(viewLifecycleOwner){
            note ->
            if(note.isNotEmpty()){
                // if note is not empty then send it to notesAdapter
                notesAdapter.differ.submitList(note)
                updateUI(note)
            }else{
                Toast.makeText(context, "Note not found", Toast.LENGTH_SHORT).show()
            }

        }
    }
    // Setup searchView
    private fun setUpSearchView(){
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterNotes(newText)
                }
                return true
            }
        })
    }
    // Implementing swipe features on recyclerView
    private fun swipeFeatures(){
        itemTouchHelper =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // get adapter position
                val position = viewHolder.adapterPosition
                // get currentNote position
                val currentNote = notesAdapter.differ.currentList[position]
                when(direction){
                    ItemTouchHelper.RIGHT -> {
                        // delete adapter
                        notesAdapter.deleteItem(position)
                        // delete current note
                        notesViewModel.delete(currentNote)
                        Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                    }
                    ItemTouchHelper.LEFT -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(
                            noteId = currentNote.id,
                            noteTitle = currentNote.title,
                            noteDescription = currentNote.description
                        )
                        findNavController().navigate(action)
                        Toast.makeText(context, "Update your note", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        // helps to attach swipe features functionality to recycler view
        itemTouchHelper?.attachToRecyclerView(binding?.recyclerView)
    }
}
package com.todo.noteverse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.todo.noteverse.databinding.RvNoteItemBinding
import com.todo.noteverse.datamodel.NotesDataModel
import com.todo.noteverse.fragments.HomeFragmentDirections

class NotesAdapter(private var context: Context): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    inner class NotesViewHolder(var binding: RvNoteItemBinding): RecyclerView.ViewHolder(binding.root)
    // helps to keep track of live data and update it to recyclerView
//    The DiffUtil.ItemCallback is used in conjunction with the AsyncListDiffer to efficiently compute the difference between two lists of items and update the RecyclerView accordingly.
    private val differCallback = object: DiffUtil.ItemCallback<NotesDataModel>(){
        override fun areItemsTheSame(oldItem: NotesDataModel, newItem: NotesDataModel): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
        override fun areContentsTheSame(oldItem: NotesDataModel, newItem: NotesDataModel): Boolean {
            return oldItem == newItem
        }
    }
    var differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = RvNoteItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.binding.apply {
            noteTitle.text = currentNote.title
            noteDesc.text = currentNote.description
        }
        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(
                noteId = currentNote.id,
                noteTitle = currentNote.title,
                noteDescription = currentNote.description
            )
            it.findNavController().navigate(action)
        }
    }
    fun deleteItem(position: Int){
        // convert current list to arraylist to delete item from a position in recyclerView
        val notesList = ArrayList(differ.currentList)
        notesList.removeAt(position)
        // submit updated list
        differ.submitList(notesList)
    }
}
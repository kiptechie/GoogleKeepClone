package com.poetcodes.googlekeepclone.ui.adapters.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.utils.HelpersUtil

class NotesAdapter(config: AsyncDifferConfig<Note>) :
    ListAdapter<Note, NotesAdapter.ViewHolder>(config) {

    private var noteClickListener: OnNoteClickListener? = null

    class ViewHolder(view: View, adapter: NotesAdapter) : RecyclerView.ViewHolder(view) {

        private val titleTv: TextView = view.findViewById(R.id.title_tv)
        private val contentTv: TextView = view.findViewById(R.id.content_tv)
        private val rootView: ConstraintLayout = view.findViewById(R.id.root_view)
        private val notesAdapter: NotesAdapter = adapter

        init {
            rootView.setOnClickListener {
                val position: Int = adapterPosition
                val note: Note = notesAdapter.getItem(adapterPosition)
                notesAdapter.performClick(note, position)
            }
        }

        fun onBind(note: Note) {
            if (HelpersUtil.isNewNote(note)) {
                rootView.visibility = View.GONE
            } else {
                rootView.visibility = View.VISIBLE
            }
            titleTv.text = note.title
            contentTv.text = note.description
        }

    }

    fun performClick(note: Note, position: Int) {
        noteClickListener?.onNoteClick(note, position)
    }

    fun setOnNoteClickListener(onNoteClickListener: OnNoteClickListener) {
        noteClickListener = onNoteClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note: Note = getItem(position)
        holder.onBind(note)
    }
}
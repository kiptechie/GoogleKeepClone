package com.poetcodes.googlekeepclone.ui.adapters.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poetcodes.googlekeepclone.R
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.utils.HelpersUtil

class NotesAdapter(config: AsyncDifferConfig<Note>) :
    ListAdapter<Note, NotesAdapter.ViewHolder>(config) {

    private var noteClickListener: OnNoteClickListener? = null
    private var noteNoteSwipeListener: OnNoteSwipeListener? = null
    private var tracker: SelectionTracker<Long>? = null
    private var selectedNotes: HashMap<String, Note> = HashMap()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = position.toLong()

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

        fun onBind(note: Note, isSelected: Boolean) {

            itemView.isActivated = isSelected

            if (isSelected) {
                notesAdapter.selectedNotes[note.id] = note
            } else {
                notesAdapter.selectedNotes.remove(note.id)
            }

            if (HelpersUtil.isNewNote(note)) {
                rootView.visibility = View.GONE
            } else {
                rootView.visibility = View.VISIBLE
            }

            if (note.title != "") {
                titleTv.visibility = View.VISIBLE
                titleTv.text = note.title
            } else {
                titleTv.visibility = View.GONE
            }

            if (note.description != "") {
                contentTv.visibility = View.VISIBLE
                contentTv.text = note.description
            } else {
                contentTv.visibility = View.GONE
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long = itemId
            }

    }

    fun performClick(note: Note, position: Int) {
        noteClickListener?.onNoteClick(note, position)
    }

    fun setOnNoteClickListener(onNoteClickListener: OnNoteClickListener) {
        noteClickListener = onNoteClickListener
    }

    fun getSelectedNotes(): List<Note> {
        val notes = selectedNotes.values
        val finalNotes: ArrayList<Note> = ArrayList()
        for (note in notes) {
            finalNotes.add(note)
        }
        return finalNotes
    }

    fun setOnNoteSwipeListener(onNoteSwipeListener: OnNoteSwipeListener) {
        noteNoteSwipeListener = onNoteSwipeListener
    }

    fun setItemSelectionTracker(tracker: SelectionTracker<Long>) {
        this.tracker = tracker
    }

    fun performNoteArchive(position: Int) {
        val note = getItem(position)
        noteNoteSwipeListener?.archiveNote(note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note: Note = getItem(position)
        tracker?.let {
            holder.onBind(note, it.isSelected(position.toLong()))
        }
    }
}
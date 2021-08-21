package com.poetcodes.googlekeepclone.ui.adapters.notes

import com.poetcodes.googlekeepclone.repository.models.entities.Note

interface OnNoteClickListener {

    fun onNoteClick(note: Note, position: Int)

}
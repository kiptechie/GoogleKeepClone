package com.poetcodes.googlekeepclone.ui.fragments.archives

import com.blankj.utilcode.util.ToastUtils
import com.poetcodes.googlekeepclone.repository.models.entities.Archive
import com.poetcodes.googlekeepclone.ui.fragments.notes.ViewEditNoteFragment

class ArchiveAdded(private val viewEditNoteFragment: ViewEditNoteFragment) : ArchiveAddListener {

    override fun archiveAdded(archive: Archive) {
        var msg = "Note"
        val note = archive.note;
        if (note.title != "") {
            msg = note.title
        }
        ToastUtils.showShort("$msg Archived!")
        viewEditNoteFragment.requireActivity().onBackPressed()
    }
}
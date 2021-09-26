package com.poetcodes.googlekeepclone.ui.fragments.archives

import com.poetcodes.googlekeepclone.repository.models.entities.Archive

interface ArchiveAddListener {
    fun archiveAdded(archive: Archive)
}
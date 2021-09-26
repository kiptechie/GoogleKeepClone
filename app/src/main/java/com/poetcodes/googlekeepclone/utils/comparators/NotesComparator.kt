package com.poetcodes.googlekeepclone.utils.comparators

import com.poetcodes.googlekeepclone.repository.models.entities.Note
import io.sentry.Sentry
import timber.log.Timber
import java.util.*

class NotesComparator : Comparator<Note> {
    override fun compare(p0: Note?, p1: Note?): Int {
        return try {
            val dateModified0: Long? = p0?.updatedAt?.toLong()
            val dateModified1: Long? = p1?.updatedAt?.toLong()
            if (dateModified0 != null && dateModified1 != null) {
                val date0 = Date(dateModified0)
                val date1 = Date(dateModified1)
                date1.compareTo(date0)
            } else {
                1
            }
        } catch (e: Exception) {
            Timber.e(e)
            Sentry.captureException(e)
            1
        }
    }
}
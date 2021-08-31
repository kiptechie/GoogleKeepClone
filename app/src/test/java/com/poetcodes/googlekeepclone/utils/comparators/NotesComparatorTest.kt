package com.poetcodes.googlekeepclone.utils.comparators

import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.utils.HelpersUtil
import junit.framework.Assert.assertEquals
import org.junit.Test

class NotesComparatorTest {

    @Test
    fun noteComparatorWorks() {
        val notes: ArrayList<Note> = ArrayList()
        for (i in 0..10) {
            val newNote = HelpersUtil.newNote()
            newNote.updatedAt = i.toString()
            notes.add(newNote)
        }
        val comparedNotes = notes.sortedWith(NotesComparator())
        val notesToReverse = notes.reversed()
        assertEquals(comparedNotes, notesToReverse)
    }
}
package com.poetcodes.googlekeepclone.utils

import com.poetcodes.googlekeepclone.repository.models.NoteEssentials
import junit.framework.Assert.assertEquals
import org.junit.Test

class HelpersUtilTest {

    @Test
    fun checkingIfNoteIsNew() {
        // new note
        val noteEssentials = NoteEssentials(
            "bleh",
            "",
            "",
            "now",
            "now now",
            false
        )
        val note = NoteEntityUtil.Builder.newInstance()
            .withNoteEssentials(noteEssentials)
            .build().note
        val isNoteNew = HelpersUtil.isNewNote(note)
        assertEquals(isNoteNew, true)
    }

    @Test
    fun checkingIfNoteIsOld() {
        // old note
        val oldNoteEssentials = NoteEssentials(
            "bleh",
            "Title",
            "Description",
            "then",
            "then then",
            false
        )
        val oldNote = NoteEntityUtil.Builder.newInstance()
            .withNoteEssentials(oldNoteEssentials)
            .build().note
        val isNewNote = HelpersUtil.isNewNote(oldNote)
        assertEquals(isNewNote, false)
    }

    @Test
    fun newNoteCreation() {
        val title = ""
        val description = ""
        val currentTime = System.currentTimeMillis().toString()
        val id = "uuid"
        val noteEssentials = NoteEssentials(
            id,
            title,
            description,
            currentTime,
            currentTime,
            false
        )
        val noteEntityUtil = NoteEntityUtil.Builder()
            .withNoteEssentials(noteEssentials)
            .build()
        val newNote = noteEntityUtil.note
        assertEquals(HelpersUtil.newNote().title, newNote.title)
        assertEquals(HelpersUtil.newNote().description, newNote.description)
    }

    @Test
    fun dateMapperInstanceIsCreated() {
        val dateMapper = HelpersUtil.dateMapperInstance()
        val myDateMapper = HelpersUtil.dateMapperInstance()
        assertEquals(dateMapper, myDateMapper)
    }
}
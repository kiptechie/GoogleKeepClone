package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.Images
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import com.poetcodes.googlekeepclone.repository.models.entities.Note

class NoteTypeConverter {

    @TypeConverter
    fun fromNote(note: Note?): String {
        var myNote = note
        if (myNote == null) {
            myNote = Note(
                "",
                "",
                "",
                "",
                "",
                "",
                Images(ArrayList()),
                "",
                Label(-1, ""),
                ""
            )
        }
        return gson.toJson(myNote)
    }

    @TypeConverter
    fun toNote(noteString: String?): Note {
        var myNoteString = noteString
        if (myNoteString == null) {
            myNoteString = ""
        }
        val noteType = object : TypeToken<Note?>() {}.type
        return gson.fromJson(myNoteString, noteType)
    }

    companion object {
        val gson = Gson()
    }

}
package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.Images
import com.poetcodes.googlekeepclone.repository.models.entities.Label
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.utils.GsonUtil

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
                "",
                false
            )
        }
        return GsonUtil.gsonInstance().toJson(myNote)
    }

    @TypeConverter
    fun toNote(noteString: String?): Note {
        var myNoteString = noteString
        if (myNoteString == null) {
            myNoteString = ""
        }
        val noteType = object : TypeToken<Note?>() {}.type
        return GsonUtil.gsonInstance().fromJson(myNoteString, noteType)
    }

}
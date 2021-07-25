package com.poetcodes.googlekeepclone.repository.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poetcodes.googlekeepclone.repository.models.entities.Note

class NoteTypeConverter {

    @TypeConverter
    fun fromNote(note: Note): String {
        return gson.toJson(note)
    }

    @TypeConverter
    fun toNote(noteString: String): Note {
        val noteType = object : TypeToken<Note?>() {}.type
        return gson.fromJson(noteString, noteType)
    }

    companion object {
        val gson = Gson()
    }

}
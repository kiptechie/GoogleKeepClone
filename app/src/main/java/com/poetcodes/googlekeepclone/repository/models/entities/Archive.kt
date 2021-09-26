package com.poetcodes.googlekeepclone.repository.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poetcodes.googlekeepclone.repository.database.converters.NoteTypeConverter

@Entity(tableName = "archives")
data class Archive(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "note") @TypeConverters(NoteTypeConverter::class) var note: Note
)
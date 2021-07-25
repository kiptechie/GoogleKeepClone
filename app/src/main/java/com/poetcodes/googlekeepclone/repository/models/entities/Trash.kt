package com.poetcodes.googlekeepclone.repository.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poetcodes.googlekeepclone.repository.database.converters.NoteTypeConverter

@Entity(tableName = "trash")
data class Trash(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "note") @TypeConverters(NoteTypeConverter::class) val note: Note
)

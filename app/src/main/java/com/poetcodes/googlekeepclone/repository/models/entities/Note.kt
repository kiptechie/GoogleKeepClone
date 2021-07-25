package com.poetcodes.googlekeepclone.repository.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poetcodes.googlekeepclone.repository.database.converters.ImagesTypeConverter
import com.poetcodes.googlekeepclone.repository.models.Images

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int?,

    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "createdAt") val createdAt: String,
    @ColumnInfo(name = "updatedAt") var updatedAt: String,

    @ColumnInfo(name = "image") var image: String?,
    @ColumnInfo(name = "images") @TypeConverters(ImagesTypeConverter::class) var images: Images?,
    @ColumnInfo(name = "deletedAt") var deletedAt: String?,
)
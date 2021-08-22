package com.poetcodes.googlekeepclone.repository.models.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poetcodes.googlekeepclone.repository.database.converters.ImagesTypeConverter
import com.poetcodes.googlekeepclone.repository.database.converters.LabelTypeConverter
import com.poetcodes.googlekeepclone.repository.models.Images
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey val id: String,

    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "createdAt") val createdAt: String,
    @ColumnInfo(name = "updatedAt") var updatedAt: String,

    @ColumnInfo(name = "image") var image: String?,
    @ColumnInfo(name = "images") @TypeConverters(ImagesTypeConverter::class) var images: Images?,
    @ColumnInfo(name = "deletedAt") var deletedAt: String?,
    @ColumnInfo(name = "label") @TypeConverters(LabelTypeConverter::class) var label: Label?,
    @ColumnInfo(name = "backgroundColor") var backgroundColor: String?,
    @ColumnInfo(name = "isPinned") var isPinned: Boolean
) : Parcelable
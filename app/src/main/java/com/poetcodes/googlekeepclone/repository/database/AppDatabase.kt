package com.poetcodes.googlekeepclone.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.poetcodes.googlekeepclone.repository.database.converters.ImagesTypeConverter
import com.poetcodes.googlekeepclone.repository.database.converters.LabelTypeConverter
import com.poetcodes.googlekeepclone.repository.database.converters.NoteTypeConverter
import com.poetcodes.googlekeepclone.repository.database.dao.*
import com.poetcodes.googlekeepclone.repository.models.entities.*

@Database(
    entities = [
        Archive::class,
        Draft::class,
        Note::class,
        Trash::class,
        Label::class
    ],
    version = 3,
    //autoMigrations = [AutoMigration(from = 2, to = 3)]
)
@TypeConverters(
    ImagesTypeConverter::class,
    LabelTypeConverter::class,
    NoteTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun archiveDao(): ArchiveDao

    abstract fun draftDao(): DraftDao

    abstract fun noteDao(): NoteDao

    abstract fun trashDao(): TrashDao

    abstract fun labelDao(): LabelDao

}
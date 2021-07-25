package com.poetcodes.googlekeepclone.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poetcodes.googlekeepclone.repository.database.dao.ArchiveDao
import com.poetcodes.googlekeepclone.repository.database.dao.DraftDao
import com.poetcodes.googlekeepclone.repository.database.dao.NoteDao
import com.poetcodes.googlekeepclone.repository.database.dao.TrashDao
import com.poetcodes.googlekeepclone.repository.models.entities.Archive
import com.poetcodes.googlekeepclone.repository.models.entities.Draft
import com.poetcodes.googlekeepclone.repository.models.entities.Note
import com.poetcodes.googlekeepclone.repository.models.entities.Trash

@Database(
    entities = [
        Archive::class,
        Draft::class,
        Note::class,
        Trash::class
    ],
    version = 1
    //autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun archiveDao(): ArchiveDao

    abstract fun draftDao(): DraftDao

    abstract fun noteDao(): NoteDao

    abstract fun trashDao(): TrashDao

}
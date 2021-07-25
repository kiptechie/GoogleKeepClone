package com.poetcodes.googlekeepclone.di

import android.content.Context
import androidx.room.Room
import com.poetcodes.googlekeepclone.repository.database.AppDatabase
import com.poetcodes.googlekeepclone.repository.database.dao.ArchiveDao
import com.poetcodes.googlekeepclone.repository.database.dao.DraftDao
import com.poetcodes.googlekeepclone.repository.database.dao.NoteDao
import com.poetcodes.googlekeepclone.repository.database.dao.TrashDao
import com.poetcodes.googlekeepclone.utils.ConstantsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideBlogDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            ConstantsUtil.DATABASE_NAME
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideArchiveDao(appDatabase: AppDatabase): ArchiveDao {
        return appDatabase.archiveDao()
    }

    @Singleton
    @Provides
    fun provideDraftDao(appDatabase: AppDatabase): DraftDao {
        return appDatabase.draftDao()
    }

    @Singleton
    @Provides
    fun provideNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao()
    }

    @Singleton
    @Provides
    fun provideTrashDao(appDatabase: AppDatabase): TrashDao {
        return appDatabase.trashDao()
    }

}
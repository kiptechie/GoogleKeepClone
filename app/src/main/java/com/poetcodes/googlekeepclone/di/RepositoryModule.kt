package com.poetcodes.googlekeepclone.di

import com.poetcodes.googlekeepclone.repository.MainRepository
import com.poetcodes.googlekeepclone.repository.database.dao.ArchiveDao
import com.poetcodes.googlekeepclone.repository.database.dao.DraftDao
import com.poetcodes.googlekeepclone.repository.database.dao.NoteDao
import com.poetcodes.googlekeepclone.repository.database.dao.TrashDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        archiveDao: ArchiveDao,
        draftDao: DraftDao,
        noteDao: NoteDao,
        trashDao: TrashDao
    ): MainRepository {
        return MainRepository(
            archiveDao,
            draftDao,
            noteDao,
            trashDao
        )
    }

}
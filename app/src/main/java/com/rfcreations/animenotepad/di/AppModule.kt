package com.rfcreations.animenotepad.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.rfcreations.animenotepad.database.NoteDataBase
import com.rfcreations.animenotepad.database.note.NoteDao
import com.rfcreations.animenotepad.database.trash.TrashDao
import com.rfcreations.animenotepad.repository.UserPreferenceRepository
import com.rfcreations.animenotepad.repository.UserPreferenceRepositoryImpl
import com.rfcreations.animenotepad.utils.ThemeUiState
import com.rfcreations.animenotepad.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNoteDataBase(@ApplicationContext context: Context): NoteDataBase {
        return Room.databaseBuilder(context, NoteDataBase::class.java, Constants.DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun providesNoteDao(noteDataBase: NoteDataBase): NoteDao {
        return noteDataBase.noteDao()
    }

    @Provides
    @Singleton
    fun providesTrashDao(noteDataBase: NoteDataBase): TrashDao {
        return noteDataBase.trashDao()
    }

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val prefName = Constants.PrefKeys.PREF_NAME
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesUserPreferenceRepository(sharedPreferences: SharedPreferences): UserPreferenceRepository {
        return UserPreferenceRepositoryImpl(sharedPreferences)
    }

    @Singleton
    @Provides
    fun providesThemeUiState(
        userPreferenceRepository: UserPreferenceRepository
    ): ThemeUiState = ThemeUiState(userPreferenceRepository)

}
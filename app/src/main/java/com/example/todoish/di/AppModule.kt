package com.example.todoish.di

import android.content.Context
import com.example.todoish.data.model.TodoDatabase
import com.example.todoish.data.repository.TodoRepository
import com.example.todoish.data.repository.TodoRepositoryImpl
import com.example.todoish.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TodoLocalRepository

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultDispatcher

    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context) =
        TodoDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideTodoRepository(
        todoDatabase: TodoDatabase
    ): TodoRepository = TodoRepositoryImpl(todoDatabase.todoDao())


    @Singleton
    @Provides
    fun provideFormatDateUseCase() = FormatDateUseCase()

    @Singleton
    @Provides
    fun provideGetUpcomingTodoUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher
    ) = GetUpcomingTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideGetCompleteTodoUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher
    ) = GetCompleteTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideGetFailedTodoUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher
    ) = GetFailedTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideTodoByDateUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher
    ) = GetLatestTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideAddTodoUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher
    ) = AddTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun providedEditTodoUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher
    ) = EditTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideUpdateTodoUseCase(repository: TodoRepository, ioDispatcher: CoroutineDispatcher) =
        UpdateTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideRemoveTodoUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher
    ) = RemoveTodoUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideGetTodoByIdUseCase(
        repository: TodoRepository,
        ioDispatcher: CoroutineDispatcher) = GetTodoByIdUseCase(repository, ioDispatcher)

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
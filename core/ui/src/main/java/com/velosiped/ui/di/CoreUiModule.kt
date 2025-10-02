package com.velosiped.ui.di

import com.velosiped.ui.model.textfieldvalidator.TextFieldValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreUiModule {
    @Provides
    @Singleton
    fun provideTextFieldValidator(): TextFieldValidator = TextFieldValidator()
}
package com.velosiped.utility.di

import android.content.Context
import com.velosiped.utility.camerahelper.CameraHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CoreUtilityModule {
    @Provides
    fun provideCameraHelper(@ApplicationContext context: Context): CameraHelper =
        CameraHelper(context = context)
}
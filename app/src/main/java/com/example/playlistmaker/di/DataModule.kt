package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.iTunesApiService
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.data.storage.LocalStorageImpl
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.storage.LocalStorage
import com.example.playlistmaker.utils.GsonJsonConverterImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<iTunesApiService> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApiService::class.java)
    }

    single<GsonJsonConverter> {
        GsonJsonConverterImpl(Gson())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<LocalStorage> {
        LocalStorageImpl(get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single {
        Room
            .databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "database.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }


}

private const val ITUNES_BASE_URL = "https://itunes.apple.com"
private const val SHARED_PREFERENCES = "SharedPrefs"
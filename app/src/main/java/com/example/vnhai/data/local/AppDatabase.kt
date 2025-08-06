package com.example.vnhai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vnhai.data.local.entity.MusicEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, MusicEntity::class, PlaylistEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun managerDao(): ManagerDao

    companion object{
    //Dam bao luon doc gia tri moi nhat
        @Volatile
        private var Instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase{
            return Instance ?: synchronized(this){
                Instance ?: Room.databaseBuilder(
                                context,
                                AppDatabase::class.java, "app_database"
                            ).fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
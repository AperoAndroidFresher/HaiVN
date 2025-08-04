package com.example.vnhai.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vnhai.model.dao.ManagerDao
import com.example.vnhai.model.entity.MusicEntity
import com.example.vnhai.model.entity.PlaylistEntity
import com.example.vnhai.model.entity.UserEntity

@Database(entities = [UserEntity::class, MusicEntity::class, PlaylistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun managerDao(): ManagerDao

    companion object{
    //Dam bao luon doc gia tri moi nhat
        @Volatile
        private var Instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase{
            return Instance ?: synchronized(this){
                Instance ?: Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java, "app_database"
                            ).fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
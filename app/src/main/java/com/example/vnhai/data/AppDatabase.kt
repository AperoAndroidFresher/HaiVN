package com.example.vnhai.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vnhai.data.ManagerDao
import com.example.vnhai.data.entity.MusicEntity
import com.example.vnhai.data.entity.PlaylistEntity
import com.example.vnhai.data.entity.UserEntity

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
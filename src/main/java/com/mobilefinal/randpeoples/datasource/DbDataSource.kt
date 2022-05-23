package com.mobilefinal.randpeoples.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobilefinal.randpeoples.model.User
import com.mobilefinal.randpeoples.model.UserDao

@Database(entities = [User::class], version = 1)
abstract class DbDataSource : RoomDatabase() {

    abstract fun userDao(): UserDao
}
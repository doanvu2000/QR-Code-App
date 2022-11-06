package com.dd.company.qrapp.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dd.company.qrapp.roomdb.model.user.UserDao
import com.dd.company.qrapp.roomdb.model.user.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDB :RoomDatabase() {
    abstract fun userDao() : UserDao
}
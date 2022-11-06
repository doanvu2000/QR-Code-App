package com.dd.company.qrapp.roomdb.model.user

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insert(userEntity: UserEntity): Long

    @Update
    fun update(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUser():Flow<UserEntity>
}
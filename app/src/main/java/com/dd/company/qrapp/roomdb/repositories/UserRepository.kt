package com.dd.company.qrapp.roomdb.repositories

import com.dd.company.qrapp.roomdb.model.user.UserDao
import com.dd.company.qrapp.roomdb.model.user.UserEntity

class UserRepository constructor(private val userDao: UserDao) {
    suspend fun insertUser(userEntity: UserEntity) = userDao.insert(userEntity)
}
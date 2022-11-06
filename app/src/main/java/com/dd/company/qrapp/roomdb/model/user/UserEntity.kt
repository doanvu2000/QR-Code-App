package com.dd.company.qrapp.roomdb.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    var name:String = "",
    var email:String = ""
)
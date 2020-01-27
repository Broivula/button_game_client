package com.example.buttongame.Database

import android.content.Context
import kotlinx.coroutines.runBlocking

object DatabaseObject{

    private var dataBase: AppDatabase? = null

    fun initDatabase(context: Context) = runBlocking {
        dataBase = AppDatabase.get(context)
    }

    fun getUserData() : List<DB_User>?  {
        return dataBase?.DB_UserDao()?.getAll()
    }

    fun addUserToDB(uid: String, username: String) {
        dataBase?.DB_UserDao()?.insert(DB_User(uid, username))
    }

}
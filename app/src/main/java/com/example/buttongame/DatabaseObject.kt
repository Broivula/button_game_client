package com.example.buttongame

import android.content.Context
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

object DatabaseObject{

    private var dataBase: AppDatabase? = null

    fun initDatabase(context: Context) = runBlocking {
        dataBase = AppDatabase.get(context)
    }

    fun getUserData () : List<DB_User>? = doAsync {
        var result = dataBase?.DB_UserDao()?.getAll()

    }

}
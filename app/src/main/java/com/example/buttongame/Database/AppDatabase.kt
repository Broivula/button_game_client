package com.example.buttongame.Database

import android.content.Context
import androidx.room.*


@Entity
data class DB_User(
    @PrimaryKey
    val uid: String,
    val username : String
)

@Dao
interface DB_UserDao {
    @Query("SELECT * FROM DB_User")
    fun getAll(): List<DB_User>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DB_User)

}

@Database(entities = [(DB_User::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun DB_UserDao(): DB_UserDao

    companion object{
        private var sInstance: AppDatabase? = null

        @Synchronized
        fun get(context: Context) : AppDatabase {
            if(sInstance == null) sInstance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database").allowMainThreadQueries().build()
            return sInstance!!
        }
    }
}
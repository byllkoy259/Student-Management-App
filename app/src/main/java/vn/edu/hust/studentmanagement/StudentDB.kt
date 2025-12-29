package vn.edu.hust.studentmanagement

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class StudentDB : RoomDatabase() {
    abstract fun studentDao(): StudentDataAccessObject

    companion object {
        @Volatile
        private var INSTANCE: StudentDB? = null

        fun getDatabase(context: Context): StudentDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDB::class.java,
                    "student_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
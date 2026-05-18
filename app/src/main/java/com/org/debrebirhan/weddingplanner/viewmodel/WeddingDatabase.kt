package com.org.debrebirhan.weddingplanner.ui.viewmodel

import android.content.Context
import androidx.room.*

@Entity(tableName = "wedding_tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isCompleted: Boolean
)

@Entity(tableName = "wedding_expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double
)

@Dao
interface WeddingDao {
    @Query("SELECT * FROM wedding_tasks")
    fun getAllTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

    @Query("DELETE FROM wedding_tasks")
    fun deleteAllTasks()

    @Query("SELECT * FROM wedding_expenses")
    fun getAllExpenses(): List<ExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expense: ExpenseEntity)

    @Query("DELETE FROM wedding_expenses")
    fun deleteAllExpenses()
}

@Database(entities = [TaskEntity::class, ExpenseEntity::class], version = 1, exportSchema = false)
abstract class WeddingDatabase : RoomDatabase() {
    abstract fun weddingDao(): WeddingDao

    companion object {
        @Volatile
        private var INSTANCE: WeddingDatabase? = null

        fun getDatabase(context: Context): WeddingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeddingDatabase::class.java,
                    "wedding_planner_db"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
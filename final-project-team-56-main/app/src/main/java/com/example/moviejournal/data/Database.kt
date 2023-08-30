package com.example.moviejournal.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)



    @Delete
    suspend fun delete(entity: T)

    @Query("SELECT * FROM ${'$'}{T::class.simpleName} ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<T>>

    @Query("SELECT * FROM watch_list WHERE movie_id = :id")
    fun getById(id: Int): LiveData<T>
}

@Entity(tableName = "journal_entry_table")
data class JournalEntry(
    @PrimaryKey
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "movie_name") val movieName: String,
    @ColumnInfo(name = "movie_poster") val moviePoster: String?,
    @ColumnInfo(name = "movie_rating") val movieRating: Float,
    @ColumnInfo(name = "user_review") val userReview: String?,
    @ColumnInfo(name = "user_rating") val userRating: Float?,
    @ColumnInfo(name = "timestamp") val timestamp: Long
) : java.io.Serializable

@Dao
interface JournalEntryDao : BaseDao<JournalEntry> {
    @Query("SELECT * FROM journal_entry_table ORDER BY timestamp DESC")
    override fun getAll(): LiveData<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(journalEntry: JournalEntry)

    @Delete
    override suspend fun delete(journalEntry: JournalEntry)
}

@Entity(tableName = "watch_list")
data class SavedMovie(
    @PrimaryKey
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "movie_name") val movieName: String,
    @ColumnInfo(name = "movie_poster") val moviePoster: String?,
    @ColumnInfo(name = "movie_rating") val movieRating: Float,
    @ColumnInfo(name = "timestamp") val timestamp: Long
) : java.io.Serializable

@Dao
interface SavedMovieDao: BaseDao<SavedMovie> {
    @Query("SELECT * FROM watch_list ORDER BY timestamp DESC")
    override fun getAll(): LiveData<List<SavedMovie>>

    @Query("SELECT * FROM watch_list WHERE movie_id = :id")
    override fun getById(id: Int): LiveData<SavedMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(savedMovie: SavedMovie)

    @Delete
    override suspend fun delete(savedMovie: SavedMovie)
}

class ApplicationRepository<T>(private val dao: BaseDao<T>) {
    val allItems: LiveData<List<T>> = dao.getAll()

    suspend fun getById(id: Int): LiveData<T> {
        return dao.getById(id)
    }

    suspend fun insert(item: T) {
        dao.insert(item)
    }

    suspend fun delete(item: T) {
        dao.delete(item)
    }

}

@Database(entities = [JournalEntry::class, SavedMovie::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalEntryDao(): JournalEntryDao
    abstract fun savedMovieDao(): SavedMovieDao
    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it}
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "movie-journal")
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Manually drop the existing watch_list table
        database.execSQL("DROP TABLE IF EXISTS watch_list")
        // Create the new table
        // Create the watch_list table with the expected schema
        database.execSQL("CREATE TABLE IF NOT EXISTS watch_list (" +
                "movie_id INTEGER PRIMARY KEY NOT NULL," +
                "movie_name TEXT NOT NULL," +
                "movie_poster TEXT," +
                "movie_rating REAL NOT NULL," +
                "timestamp INTEGER NOT NULL)")


    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Manually drop the existing watch_list table
        database.execSQL("DROP TABLE IF EXISTS watch_list")
        // Create the new table
        // Create the watch_list table with the expected schema
        database.execSQL("CREATE TABLE IF NOT EXISTS watch_list (" +
                "movie_id INTEGER PRIMARY KEY NOT NULL," +
                "movie_name TEXT NOT NULL," +
                "movie_poster TEXT," +
                "timestamp INTEGER NOT NULL)")

    }
}


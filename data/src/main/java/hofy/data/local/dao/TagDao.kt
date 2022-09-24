package hofy.data.local.dao

import androidx.room.*
import hofy.data.local.model.TagDB

@Dao
interface TagDao {
    @Query("SELECT * FROM tagdb")
    fun getAll(): List<TagDB>

    @Query("SELECT * FROM tagdb WHERE name LIKE :query")
    fun findByName(query: String): List<TagDB>

    @Query("SELECT color FROM tagdb")
    fun getAllColors(): List<Long>

    @Query("SELECT * FROM tagdb WHERE color LIKE :query")
    fun findByColor(query: String): List<TagDB>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg tags: TagDB)

    @Delete
    fun delete(tags: TagDB)
}
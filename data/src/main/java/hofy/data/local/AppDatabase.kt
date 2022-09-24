package hofy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import hofy.data.local.dao.TagDao
import hofy.data.local.model.TagDB

@Database(entities = [TagDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}
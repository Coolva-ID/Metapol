package id.coolva.metapol.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.coolva.metapol.core.data.entity.SIMRegistrationEntity

@Database(
    entities = [SIMRegistrationEntity::class],
    version = 1,
    exportSchema = false
)

abstract class MetapolDatabase: RoomDatabase() {
    abstract fun metapolDao(): MetapolDao
}
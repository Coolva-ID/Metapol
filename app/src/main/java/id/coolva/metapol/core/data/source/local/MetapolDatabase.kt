package id.coolva.metapol.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.coolva.metapol.core.data.entity.*

@Database(
    entities = [SIMRegistrationEntity::class, EscortReqEntity::class, SKCKRegEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class MetapolDatabase : RoomDatabase() {
    abstract fun metapolDao(): MetapolDao
}
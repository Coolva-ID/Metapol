package id.coolva.metapol.core.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.coolva.metapol.core.data.entity.EscortReqEntity
import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetapolDao {
    /**
     * SIM Registration
     */
    @Query("SELECT * FROM sim_registration")
    fun getSIMRegistration(): Flow<List<SIMRegistrationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSIMRegistration(simRegistrationEntity: SIMRegistrationEntity)

    @Query("DELETE FROM sim_registration")
    suspend fun deleteSIMReg()

    /**
     * Escort Request
     */
    @Query("SELECT * FROM escort_request")
    fun getEscortRequestList(): Flow<List<EscortReqEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEscortReq(escortReqEntity: EscortReqEntity)

    @Query("DELETE FROM escort_request")
    suspend fun deleteAllEscortReq()
}
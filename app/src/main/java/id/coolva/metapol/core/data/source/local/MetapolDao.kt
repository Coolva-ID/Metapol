package id.coolva.metapol.core.data.source.local

import androidx.room.*
import id.coolva.metapol.core.data.entity.EscortReqEntity
import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import id.coolva.metapol.core.data.entity.SKCKRegEntity
import id.coolva.metapol.core.data.entity.UserEntity
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

    /**
     * SKCK Registration
     */
    @Query("SELECT * FROM skck_registration")
    fun getSKCKRegList(): Flow<List<SKCKRegEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSKCKReg(skckRegEntity: SKCKRegEntity)

    @Query("DELETE FROM skck_registration")
    suspend fun deleteAllSKCKReg()

    /**
     * User Data
     */
    @Query("SELECT * FROM user_table")
    fun getUserList(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(userEntity: UserEntity)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUser()

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)
}
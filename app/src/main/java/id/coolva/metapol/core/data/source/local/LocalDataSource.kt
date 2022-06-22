package id.coolva.metapol.core.data.source.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.coolva.metapol.core.data.entity.EscortReqEntity
import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import id.coolva.metapol.core.data.entity.SKCKRegEntity
import id.coolva.metapol.core.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: MetapolDao) {

    /**
     * SIM Registration
     */
    fun getSIMRegistration(): Flow<List<SIMRegistrationEntity>> = dao.getSIMRegistration()

    suspend fun insertSIMRegistration(data: SIMRegistrationEntity) = dao.insertSIMRegistration(data)

    suspend fun deleteSIMReg() = dao.deleteSIMReg()

    /**
     * Escort Request
     */
    fun getEscortRequestList(): Flow<List<EscortReqEntity>> = dao.getEscortRequestList()

    suspend fun insertEscortReq(escortReqEntity: EscortReqEntity) =
        dao.insertEscortReq(escortReqEntity)

    suspend fun deleteAllEscortReq() = dao.deleteAllEscortReq()

    /**
     * SKCK Registration
     */
    fun getSKCKRegList(): Flow<List<SKCKRegEntity>> = dao.getSKCKRegList()

    suspend fun insertSKCKReg(skckRegEntity: SKCKRegEntity) = dao.insertSKCKReg(skckRegEntity)

    suspend fun deleteAllSKCKReg() = dao.deleteAllSKCKReg()

    /**
     * User Data
     */
    fun getUserList(): Flow<List<UserEntity>> = dao.getUserList()

    suspend fun insertUser(userEntity: UserEntity) = dao.insertUser(userEntity)

    suspend fun updateUser(userEntity: UserEntity) = dao.updateUser(userEntity)

    suspend fun deleteAllUser() = dao.deleteAllUser()

    suspend fun deleteUser(userEntity: UserEntity) = dao.deleteUser(userEntity)
}
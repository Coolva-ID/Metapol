package id.coolva.metapol.core.domain.repository

import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.core.domain.model.SKCKReg
import id.coolva.metapol.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IMetapolRepository {

    /**
     * SIM Registration
     */
    fun getSIMRegistration(): Flow<List<SIMRegsitration>>
    suspend fun insertSIMRegistration(simReg: SIMRegsitration)
    suspend fun deleteSIMReg()


    /**
     * Escort Request
     */
    fun getEscortRequestList(): Flow<List<EscortReq>>
    suspend fun insertEscortReq(escortReq: EscortReq)
    suspend fun deleteAllEscortReq()


    /**
     * SKCK Registration
     */
    fun getSKCKRegList(): Flow<List<SKCKReg>>
    suspend fun insertSKCKReg(skckReg: SKCKReg)
    suspend fun deleteAllSKCKReg()


    /**
     * User Data
     */
    fun getUserList(): Flow<List<User>>
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteAllUser()
    suspend fun deleteUser(user: User)
}
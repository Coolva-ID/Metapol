package id.coolva.metapol.core.domain.repository

import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration
import kotlinx.coroutines.flow.Flow

interface IMetapolRepository {

    fun getSIMRegistration(): Flow<List<SIMRegsitration>>

    suspend fun insertSIMRegistration(data: SIMRegsitration)

    suspend fun deleteSIMReg()

    fun getEscortRequestList(): Flow<List<EscortReq>>

    suspend fun insertEscortReq(escortReqEntity: EscortReq)

    suspend fun deleteAllEscortReq()
}
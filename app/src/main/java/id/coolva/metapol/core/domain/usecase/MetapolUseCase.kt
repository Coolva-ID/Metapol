package id.coolva.metapol.core.domain.usecase

import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration
import kotlinx.coroutines.flow.Flow

interface MetapolUseCase {

    fun getSIMRegistration(): Flow<List<SIMRegsitration>>

    suspend fun insertSIMRegistration(data: SIMRegsitration)

    suspend fun deleteSIMReg()

    fun getEscortRequestList(): Flow<List<EscortReq>>

    suspend fun insertEscortReq(escortReq: EscortReq)

    suspend fun deleteAllEscortReq()

}
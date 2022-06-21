package id.coolva.metapol.core.domain.usecase

import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.core.domain.repository.IMetapolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MetapolInteractor @Inject constructor(private val repository: IMetapolRepository): MetapolUseCase {
    override fun getSIMRegistration(): Flow<List<SIMRegsitration>> {
        return repository.getSIMRegistration()
    }

    override suspend fun insertSIMRegistration(data: SIMRegsitration) {
        repository.insertSIMRegistration(data)
    }

    override suspend fun deleteSIMReg() {
        repository.deleteSIMReg()
    }

    override fun getEscortRequestList(): Flow<List<EscortReq>> {
        return repository.getEscortRequestList()
    }

    override suspend fun insertEscortReq(escortReq: EscortReq) {
        repository.insertEscortReq(escortReq)
    }

    override suspend fun deleteAllEscortReq() {
        repository.deleteAllEscortReq()
    }
}
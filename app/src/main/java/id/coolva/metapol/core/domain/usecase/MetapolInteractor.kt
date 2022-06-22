package id.coolva.metapol.core.domain.usecase

import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.core.domain.model.SKCKReg
import id.coolva.metapol.core.domain.model.User
import id.coolva.metapol.core.domain.repository.IMetapolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MetapolInteractor @Inject constructor(private val repository: IMetapolRepository): MetapolUseCase {

    /**
     * SIM Registration
     */
    override fun getSIMRegistration(): Flow<List<SIMRegsitration>> {
        return repository.getSIMRegistration()
    }

    override suspend fun insertSIMRegistration(data: SIMRegsitration) {
        repository.insertSIMRegistration(data)
    }

    override suspend fun deleteSIMReg() {
        repository.deleteSIMReg()
    }


    /**
     * Escort Request
     */
    override fun getEscortRequestList(): Flow<List<EscortReq>> {
        return repository.getEscortRequestList()
    }

    override suspend fun insertEscortReq(escortReq: EscortReq) {
        repository.insertEscortReq(escortReq)
    }

    override suspend fun deleteAllEscortReq() {
        repository.deleteAllEscortReq()
    }


    /**
     * SKCK Registration
     */
    override fun getSKCKRegList(): Flow<List<SKCKReg>> {
        return repository.getSKCKRegList()
    }

    override suspend fun insertSKCKReg(skckReg: SKCKReg) {
        repository.insertSKCKReg(skckReg)
    }

    override suspend fun deleteAllSKCKReg() {
        repository.deleteAllSKCKReg()
    }


    /**
     * User Data
     */
    override fun getUserList(): Flow<List<User>> {
        return repository.getUserList()
    }

    override suspend fun insertUser(user: User) {
        repository.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        repository.updateUser(user)
    }

    override suspend fun deleteAllUser() {
        repository.deleteAllUser()
    }

    override suspend fun deleteUser(user: User) {
        repository.deleteUser(user)
    }
}
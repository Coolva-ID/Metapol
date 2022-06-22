package id.coolva.metapol.core.data.source

import id.coolva.metapol.core.data.entity.EscortReqEntity
import id.coolva.metapol.core.data.source.local.LocalDataSource
import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.core.domain.model.SKCKReg
import id.coolva.metapol.core.domain.model.User
import id.coolva.metapol.core.domain.repository.IMetapolRepository
import id.coolva.metapol.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MetapolRepository @Inject constructor(private val localDataSource: LocalDataSource) :
    IMetapolRepository {

    /**
     * SIM Registration
     */
    override fun getSIMRegistration(): Flow<List<SIMRegsitration>> {
        return localDataSource.getSIMRegistration()
            .map { DataMapper.mapSIMRegEntityListToDomain(it) }
    }

    override suspend fun insertSIMRegistration(data: SIMRegsitration) {
        localDataSource.insertSIMRegistration(
            DataMapper.mapSIMRegToEntity(data)
        )
    }

    override suspend fun deleteSIMReg() {
        localDataSource.deleteSIMReg()
    }

    /**
     * Escort Request
     */
    override fun getEscortRequestList(): Flow<List<EscortReq>> {
        return localDataSource.getEscortRequestList()
            .map { DataMapper.mapEscortReqEntityListToDomain(it) }
    }

    override suspend fun insertEscortReq(escortReq: EscortReq) {
        localDataSource.insertEscortReq(
            DataMapper.mapEscortReqToEntity(escortReq)
        )
    }

    override suspend fun deleteAllEscortReq() {
        localDataSource.deleteAllEscortReq()
    }

    /**
     * SKCK Registration
     */
    override fun getSKCKRegList(): Flow<List<SKCKReg>> {
        return localDataSource.getSKCKRegList()
            .map { DataMapper.mapSKCKRegEntityListToDomain(it) }
    }

    override suspend fun insertSKCKReg(skckReg: SKCKReg) {
        localDataSource.insertSKCKReg(DataMapper.mapSKCKRegDomainToEntity(skckReg))
    }

    override suspend fun deleteAllSKCKReg() {
        localDataSource.deleteAllSKCKReg()
    }

    /**
     * User Data
     */
    override fun getUserList(): Flow<List<User>> {
        return localDataSource.getUserList().map { DataMapper.mapUserEntityListToDomain(it) }
    }

    override suspend fun insertUser(user: User) {
        localDataSource.insertUser(DataMapper.mapUserDomainToEntity(user))
    }

    override suspend fun updateUser(user: User) {
        localDataSource.updateUser(DataMapper.mapUserDomainToEntity(user))
    }

    override suspend fun deleteAllUser() {
        localDataSource.deleteAllUser()
    }

    override suspend fun deleteUser(user: User) {
        localDataSource.deleteUser(DataMapper.mapUserDomainToEntity(user))
    }

}
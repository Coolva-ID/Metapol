package id.coolva.metapol.core.data.source.local

import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: MetapolDao) {

    fun getSIMRegistration(): Flow<List<SIMRegistrationEntity>> = dao.getSIMRegistration()

    suspend fun insertSIMRegistration(data: SIMRegistrationEntity) = dao.insertSIMRegistration(data)
}
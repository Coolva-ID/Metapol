package id.coolva.metapol.core.domain.repository

import id.coolva.metapol.core.domain.model.SIMRegsitration
import kotlinx.coroutines.flow.Flow

interface IMetapolRepository {

    fun getSIMRegistration(): Flow<List<SIMRegsitration>>

    suspend fun insertSIMRegistration(data: SIMRegsitration)
}
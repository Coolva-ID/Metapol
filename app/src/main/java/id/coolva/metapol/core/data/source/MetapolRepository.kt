package id.coolva.metapol.core.data.source

import id.coolva.metapol.core.data.source.local.LocalDataSource
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.core.domain.repository.IMetapolRepository
import id.coolva.metapol.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MetapolRepository @Inject constructor(private val localDataSource: LocalDataSource) : IMetapolRepository {

    override fun getSIMRegistration(): Flow<List<SIMRegsitration>> {
        return localDataSource.getSIMRegistration()
            .map { DataMapper.mapSIMRegEntityListToDomain(it) }
    }

    override suspend fun insertSIMRegistration(data: SIMRegsitration) {
        localDataSource.insertSIMRegistration(
            DataMapper.mapSIMRegToEntity(data)
        )
    }
}
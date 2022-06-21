package id.coolva.metapol.core.data.source.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.coolva.metapol.core.data.entity.EscortReqEntity
import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: MetapolDao) {

    fun getSIMRegistration(): Flow<List<SIMRegistrationEntity>> = dao.getSIMRegistration()

    suspend fun insertSIMRegistration(data: SIMRegistrationEntity) = dao.insertSIMRegistration(data)

    suspend fun deleteSIMReg() = dao.deleteSIMReg()

    fun getEscortRequestList(): Flow<List<EscortReqEntity>> = dao.getEscortRequestList()

    suspend fun insertEscortReq(escortReqEntity: EscortReqEntity) =
        dao.insertEscortReq(escortReqEntity)

    suspend fun deleteAllEscortReq() = dao.deleteAllEscortReq()
}
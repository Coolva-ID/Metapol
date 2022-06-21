package id.coolva.metapol.utils

import id.coolva.metapol.core.data.entity.EscortReqEntity
import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration

object DataMapper {

    /**
     *  MAPPING SIM Registration Data
     */
    private fun mapSIMRegEntityToDomain(data: SIMRegistrationEntity): SIMRegsitration {
        return SIMRegsitration(
            data.simRegId,
            data.golonganSIM,
            data.memilikiSertMengemudi,
            data.ttdPhotoPath,
            data.pasPhotoPath,
            data.contactName,
            data.contactAddress,
            data.contactPhoneNo,
            data.status
        )
    }

    fun mapSIMRegEntityListToDomain(data: List<SIMRegistrationEntity>): List<SIMRegsitration> {
        return data.map { mapSIMRegEntityToDomain(it) }
    }

    fun mapSIMRegToEntity(data: SIMRegsitration): SIMRegistrationEntity {
        return SIMRegistrationEntity(
            data.simRegId,
            data.golonganSIM,
            data.memilikiSertMengemudi,
            data.ttdPhotoPath,
            data.pasPhotoPath,
            data.contactName,
            data.contactAddress,
            data.contactPhoneNo,
            data.status
        )
    }


    /**
     *  MAPPING Escort Request Data
     */
    fun mapEscortReqEntityListToDomain(data: List<EscortReqEntity>): List<EscortReq> {
        return data.map { mapEscortReqEntityToDomain(it) }
    }

    private fun mapEscortReqEntityToDomain(data: EscortReqEntity): EscortReq {
        return EscortReq(
            data.escortReqId,
            data.escortPurpose,
            data.departureDate,
            data.departureTime,
            data.departurePoint,
            data.destinationPoint,
            data.jumlahDikawal,
            data.escortVehicleType,
            data.numOfEscortVehicle,
            data.status
        )
    }

    fun mapEscortReqToEntity(data: EscortReq): EscortReqEntity{
        return EscortReqEntity(
            data.escortReqId,
            data.escortPurpose,
            data.departureDate,
            data.departureTime,
            data.departurePoint,
            data.destinationPoint,
            data.jumlahDikawal,
            data.escortVehicleType,
            data.numOfEscortVehicle,
            data.status
        )
    }
}
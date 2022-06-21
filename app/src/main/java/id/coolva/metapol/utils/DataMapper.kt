package id.coolva.metapol.utils

import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import id.coolva.metapol.core.domain.model.SIMRegsitration

object DataMapper {

    /**
     *  MAPPING SIM Registration Data
     */
    private fun mapSIMRegEntityToDomain(data: SIMRegistrationEntity): SIMRegsitration {
        return SIMRegsitration(
            data.id,
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
            data.id,
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
}
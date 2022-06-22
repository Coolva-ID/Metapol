package id.coolva.metapol.utils

import id.coolva.metapol.core.data.entity.EscortReqEntity
import id.coolva.metapol.core.data.entity.SIMRegistrationEntity
import id.coolva.metapol.core.data.entity.SKCKRegEntity
import id.coolva.metapol.core.data.entity.UserEntity
import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.core.domain.model.SKCKReg
import id.coolva.metapol.core.domain.model.User

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


    /**
     * MAPPING SKCK Registration
     */
    private fun mapSKCKRegEntityToDomain(data: SKCKRegEntity): SKCKReg {
        return SKCKReg(
            data.skckId,
            data.identityCardPhotoPath,
            data.aktePhotoPath,
            data.kkPhotoPath,
            data.sidikJariPhotoPath,
            data.pasPhotoPath,
            data.status
        )
    }

    fun mapSKCKRegEntityListToDomain(data: List<SKCKRegEntity>): List<SKCKReg> {
        return data.map { mapSKCKRegEntityToDomain(it) }
    }

    fun mapSKCKRegDomainToEntity(data: SKCKReg): SKCKRegEntity {
        return SKCKRegEntity(
            data.skckId,
            data.identityCardPhotoPath,
            data.aktePhotoPath,
            data.kkPhotoPath,
            data.sidikJariPhotoPath,
            data.pasPhotoPath,
            data.status
        )
    }


    /**
     * MAPPING User Data
     */
    fun mapUserEntityToDomain(data: UserEntity): User {
        return User(
            data.userId,
            data.name,
            data.email,
            data.password,
            data.phoneNumber,
            data.profilePhoto
        )
    }

    fun mapUserEntityListToDomain(data: List<UserEntity>): List<User> {
        return data.map { mapUserEntityToDomain(it) }
    }

    fun mapUserDomainToEntity(data: User): UserEntity {
        return UserEntity(
            data.userId,
            data.name,
            data.email,
            data.password,
            data.phoneNumber,
            data.profilePhoto
        )
    }
}
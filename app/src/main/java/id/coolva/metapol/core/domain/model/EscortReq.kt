package id.coolva.metapol.core.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
data class EscortReq (
    val escortReqId: Int = 0,
    val escortPurpose: String,
    val departureDate: String,
    val departureTime: String,
    val departurePoint: String,
    val destinationPoint: String,
    val jumlahDikawal: String,
    val escortVehicleType: String,
    val numOfEscortVehicle: Int,
    var status: String
): Parcelable

package id.coolva.metapol.core.data.testing

class EscortReg (
    val uid: String,
    val escortReqId: Int = 0,
    val escortPurpose: String,
    val departureDate: String,
    val departureTime: String,
    val departurePoint: String,
    val destinationPoint: String,
    val jumlahDikawal: String,
    val escortVehicleType: String,
    val numOfEscortVehicle: Int,
    var status: String,
    val pengajuanAt: String
        )
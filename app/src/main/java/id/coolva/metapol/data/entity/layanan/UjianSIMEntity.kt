package id.coolva.metapol.data.entity.layanan

data class UjianSIMEntity (
    val id: Int,
    val golonganSIM: String,
    val memilikiSertMengemudi: Boolean,
    val ttdPhotoPath: String,
    val pasPhotoPath: String,
    val contactName: String,
    val contactAddress: String,
    val contactPhoneNo: String,
    val status: String
)
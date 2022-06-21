package id.coolva.metapol.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SIMRegsitration(

    val simRegId: Int = 0,
    val golonganSIM: String,
    val memilikiSertMengemudi: Boolean,
    val ttdPhotoPath: String,
    val pasPhotoPath: String,
    val contactName: String,
    val contactAddress: String,
    val contactPhoneNo: String,
    val status: String
) : Parcelable
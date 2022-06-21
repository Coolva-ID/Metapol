package id.coolva.metapol.core.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "sim_registration")
@Parcelize
data class SIMRegistrationEntity (
    @PrimaryKey(autoGenerate = true)
    val simRegId: Int = 0,
    val golonganSIM: String,
    val memilikiSertMengemudi: Boolean,
    val ttdPhotoPath: String,
    val pasPhotoPath: String,
    val contactName: String,
    val contactAddress: String,
    val contactPhoneNo: String,
    var status: String
): Parcelable
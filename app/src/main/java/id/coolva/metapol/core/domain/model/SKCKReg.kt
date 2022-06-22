package id.coolva.metapol.core.domain.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
data class SKCKReg(
    val skckId: Int = 0,
    val identityCardPhotoPath: String,
    val aktePhotoPath: String,
    val kkPhotoPath: String,
    val pasPhotoPath: String,
    var status: String
) : Parcelable

package id.coolva.metapol.core.domain.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: Int = 0,
    var name: String,
    var email: String,
    var password: String,
    var phoneNumber: String,
    var profilePhoto: ByteArray,
): Parcelable

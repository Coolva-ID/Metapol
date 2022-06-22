package id.coolva.metapol.core.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    var name: String,
    var email: String,
    var password: String,
    var phoneNumber: String? = null,
    var profilePhoto: ByteArray? = null,
): Parcelable

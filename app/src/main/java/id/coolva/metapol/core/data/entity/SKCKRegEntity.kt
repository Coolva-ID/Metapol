package id.coolva.metapol.core.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "skck_registration")
@Parcelize
data class SKCKRegEntity(
    @PrimaryKey(autoGenerate = true)
    val skckId: Int = 0,
    val identityCardPhotoPath: String,
    val aktePhotoPath: String,
    val kkPhotoPath: String,
    val sidikJariPhotoPath: String,
    val pasPhotoPath: String,
    var status: String
) : Parcelable

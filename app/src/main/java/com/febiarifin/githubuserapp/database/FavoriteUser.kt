package com.febiarifin.githubuserapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid") var uid: Int? = null ,
    @ColumnInfo(name = "username") var username: String? = null,
    @ColumnInfo(name = "avatar_url") var avatar_url: String? = null
): Parcelable

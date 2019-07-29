package me.tankery.justnote.data.db.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "create_timestamp")
    @SerializedName("create_timestamp")
    val createTimestamp: Calendar = Calendar.getInstance(),

    @ColumnInfo(name = "preserved")
    @SerializedName("preserved")
    val preserved: Boolean = false
)
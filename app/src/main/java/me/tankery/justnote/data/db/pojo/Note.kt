package me.tankery.justnote.data.db.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = "note",
    indices = [Index("update_timestamp")]
)
data class Note(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "create_timestamp")
    @SerializedName("create_timestamp")
    val createTimestamp: Calendar = Calendar.getInstance(),

    @ColumnInfo(name = "update_timestamp")
    @SerializedName("update_timestamp")
    val updateTimestamp: Calendar = createTimestamp,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String? = null,

    @ColumnInfo(name = "abstract_content")
    @SerializedName("abstract_content")
    val abstractContent: String? = null
)
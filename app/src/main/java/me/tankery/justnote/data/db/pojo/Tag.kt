package me.tankery.justnote.data.db.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "create_timestamp") val createTimestamp: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "preserved") val preserved: Boolean = false
)
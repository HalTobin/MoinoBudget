package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "labels")
data class Label(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "label_id") val id: Int,
    val name: String,
    val color: Int,
)
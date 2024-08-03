package data.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "labels")
data class Label(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val color: Int
)
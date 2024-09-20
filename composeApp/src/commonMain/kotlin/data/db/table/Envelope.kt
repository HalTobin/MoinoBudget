package data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "envelopes")
data class Envelope(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "envelope_id") val id: Int = 0,
    val title: String,
    val max: Int?,
    val iconId: Int?,
    val frequency: Int, // 0: Monthly, 1: Quarterly, 2: Biannually, 3: Annually
    val color: Int?
)
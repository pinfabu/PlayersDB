package com.pinfabu.playersdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pinfabu.playersdb.util.Constants

@Entity(tableName = Constants.DATABASE_PLAYER_TABLE)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "player_id")
    val id: Long = 0,
    @ColumnInfo(name = "player_player")
    var player: String,
    @ColumnInfo(name = "player_team")
    var team: String,
    @ColumnInfo(name = "player_nationality", defaultValue = "Desconocido")
    var nationality: String
)

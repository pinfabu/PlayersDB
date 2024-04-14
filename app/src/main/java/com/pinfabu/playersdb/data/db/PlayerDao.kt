package com.pinfabu.playersdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pinfabu.playersdb.data.db.model.PlayerEntity
import com.pinfabu.playersdb.util.Constants

@Dao
interface PlayerDao {

    @Insert
    suspend fun insertPlayer(player: PlayerEntity)

    @Insert
    suspend fun insertPlayer(players: List<PlayerEntity>)

    @Query("SELECT * FROM ${Constants.DATABASE_PLAYER_TABLE}")
    suspend fun getAllPlayers(): List<PlayerEntity>

    @Update
    suspend fun updatePlayer(player: PlayerEntity)

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)
}
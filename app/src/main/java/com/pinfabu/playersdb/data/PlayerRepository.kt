package com.pinfabu.playersdb.data

import com.pinfabu.playersdb.data.db.PlayerDao
import com.pinfabu.playersdb.data.db.model.PlayerEntity

class PlayerRepository(private val playerDao: PlayerDao) {

    suspend fun insertPlayer(player: PlayerEntity){
        playerDao.insertPlayer(player)
    }

    suspend fun getAllPlayers(): List<PlayerEntity>{
        return playerDao.getAllPlayers()
    }

    suspend fun updatePlayer(player: PlayerEntity){
        playerDao.updatePlayer(player)
    }

    suspend fun deletePlayer(player: PlayerEntity){
        playerDao.deletePlayer(player)
    }

}
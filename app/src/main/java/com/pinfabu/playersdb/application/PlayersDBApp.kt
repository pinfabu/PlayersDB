package com.pinfabu.playersdb.application

import android.app.Application
import com.pinfabu.playersdb.data.PlayerRepository
import com.pinfabu.playersdb.data.db.PlayerDatabase

class PlayersDBApp: Application() {
    private val database by lazy {
        PlayerDatabase.getDatabase(this@PlayersDBApp)
    }

    val repository by lazy {
        PlayerRepository(database.playerDao())
    }

}
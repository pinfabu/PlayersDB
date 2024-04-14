package com.pinfabu.playersdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pinfabu.playersdb.R
import com.pinfabu.playersdb.application.PlayersDBApp
import com.pinfabu.playersdb.data.PlayerRepository
import com.pinfabu.playersdb.data.db.model.PlayerEntity
import com.pinfabu.playersdb.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var players: List<PlayerEntity> = emptyList()
    private lateinit var repository: PlayerRepository

    private lateinit var playerAdapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as PlayersDBApp).repository
        //val url = Constants.BASE_URL
        //Log.d(Constants.LOGTAG, "Hola")

        playerAdapter = PlayerAdapter() { selectedPlayer ->
            //Click para actualizar o borrar un elemento

            val dialog = PlayerDialog(
                newPlayer = false,
                player = selectedPlayer,
                updateUI = {
                    updateUI()
                }, message = { action ->
                    message(action)
                })

            dialog.show(supportFragmentManager, "updateDialog")

        }

        binding.rvPlayers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = playerAdapter
        }


        updateUI()

    }

    private fun updateUI() {
        lifecycleScope.launch {
            players = repository.getAllPlayers()

            binding.tvNoRecords.visibility =
                if (players.isEmpty()) View.VISIBLE else View.INVISIBLE

            playerAdapter.updateList(players)

        }
    }

    //Click del floating action button para añadir un registro
    fun click(view: View) {
        //Manejo el click del FAB
        val dialog = PlayerDialog(
            updateUI = {
                updateUI()
            }, message = { action ->
                message(action)
            })
        dialog.show(supportFragmentManager, "insertDialog")
    }

    private fun message(text: String) {
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()

    }

}
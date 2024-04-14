package com.pinfabu.playersdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.pinfabu.playersdb.R
import com.pinfabu.playersdb.data.db.model.PlayerEntity
import com.pinfabu.playersdb.databinding.PlayerElementBinding

class PlayerViewHolder(private val binding: PlayerElementBinding): RecyclerView.ViewHolder(binding.root) {
    val ivIcon = binding.ivIcon

    fun bind(player: PlayerEntity){
        binding.apply {
            tvPlayer.text = player.player
            tvTeam.text = player.team
            tvNationality.text = player.nationality

            // Cambiar la imagen según el jugador seleccionado
            ivIcon.setImageResource(getPlayerImageResource(player.player))
        }
    }

    private fun getPlayerImageResource(playerName: String): Int {
        return when (playerName) {
            "Messi" -> R.drawable.messi
            "Cristiano" -> R.drawable.cristiano
            "Frenkie" -> R.drawable.frenkie
            else -> R.drawable.messi // Imagen predeterminada en caso de que el jugador no coincida con ninguno de los anteriores
        }
    }
}
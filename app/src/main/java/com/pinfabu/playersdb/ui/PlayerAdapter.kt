package com.pinfabu.playersdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinfabu.playersdb.data.db.model.PlayerEntity
import com.pinfabu.playersdb.databinding.PlayerElementBinding

class PlayerAdapter(private val onPlayerClicked: (PlayerEntity) -> Unit): RecyclerView.Adapter<PlayerViewHolder>() {

    private var players: List<PlayerEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = PlayerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int = players.size


    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {

        val player = players[position]

        holder.bind(player)

        /*holder.ivIcon.setOnClickListener {
            //Click para el imageview ivIcon
        }*/

        holder.itemView.setOnClickListener {
            //Aquí va el click a cada elemento
            onPlayerClicked(player)
        }

    }

    //Para actualizar el recyclerview
    fun updateList(list: List<PlayerEntity>){
        players = list
        notifyDataSetChanged()
    }

}
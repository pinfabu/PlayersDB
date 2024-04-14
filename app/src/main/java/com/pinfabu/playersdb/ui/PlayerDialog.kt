package com.pinfabu.playersdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.pinfabu.playersdb.R
import com.pinfabu.playersdb.application.PlayersDBApp
import com.pinfabu.playersdb.data.PlayerRepository
import com.pinfabu.playersdb.data.db.model.PlayerEntity
import com.pinfabu.playersdb.databinding.PlayerDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class PlayerDialog(
    private val newPlayer: Boolean = true,
    private var player: PlayerEntity = PlayerEntity(
        player = "",
        team = "",
        nationality = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
) : DialogFragment() {
    private var _binding: PlayerDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private lateinit var playerImageView: ImageView

    private var saveButton: Button? = null

    private lateinit var repository: PlayerRepository

    //Aquí se crea y configura el diálogo de forma inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = PlayerDialogBinding.inflate(requireActivity().layoutInflater)

        builder = AlertDialog.Builder(requireContext())

        repository = (requireContext().applicationContext as PlayersDBApp).repository

        // Configura el adaptador para el Spinner
        val playersOptions = arrayOf("Select a player", "Messi", "Cristiano", "Frenkie")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, playersOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spPlayer.adapter = adapter

        var selectedPlayer: String? = null // Variable local para almacenar el jugador seleccionado

        // Escucha los cambios en la selección del Spinner
        binding.spPlayer.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Actualiza la variable selectedPlayer con el valor seleccionado del Spinner
                saveButton?.isEnabled = validateFields()

                // Guardar la selección del usuario en la variable selectedPlayer
                selectedPlayer = playersOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario hacer nada aquí si no se selecciona nada

            }
        })

        binding.apply {
            //binding.tietPlayer.setText(player.player)

            binding.tietTeam.setText(player.team)
            binding.tietNationality.setText(player.nationality)
            //playerImageView = binding.playerElement.ivIcon
        }

        dialog = if (newPlayer)
            buildDialog(context?.getString(R.string.textSave) ?: "Save", context?.getString(R.string.textCancel) ?: "Cancel", {
                //Acción guardar
                player.apply {
                    //player = binding.tietPlayer.text.toString()
                    player = selectedPlayer ?: "" // Asigna el valor seleccionado del Spinner a la propiedad player

                    team = binding.tietTeam.text.toString()
                    nationality = binding.tietNationality.text.toString()
                }
                try {
                    lifecycleScope.launch {

                        val result = async{
                            repository.insertPlayer(player)
                        }

                        result.await()

                        withContext(Dispatchers.Main){
                            message(context?.getString(R.string.textPlayerSaved) ?: "Game saved successfully")

                            updateUI()
                        }

                    }

                }catch(e: IOException){
                    e.printStackTrace()

                    message(context?.getString(R.string.textErrorPlayerSaved) ?: "Error saving game")

                }
            }, {
                //Acción cancelar

            })
        else
            buildDialog(context?.getString(R.string.textUpdate) ?: "Update", context?.getString(R.string.textDelete) ?: "Delete", {
                //Acción actualizar
                player.apply {
                    //player = binding.tietPlayer.text.toString()
                    player = selectedPlayer ?: "" // Asigna el valor seleccionado del Spinner a la propiedad player
                    team = binding.tietTeam.text.toString()
                    nationality = binding.tietNationality.text.toString()
                }
                try {
                    lifecycleScope.launch {
                        val result = async {
                            repository.updatePlayer(player)
                        }

                        result.await()

                        withContext(Dispatchers.Main){
                            message(context?.getString(R.string.textPlayerUpdated) ?: "Player saved successfully")
                            updateUI()
                        }
                    }

                }catch(e: IOException) {
                    e.printStackTrace()

                    message(context?.getString(R.string.textErrorPlayerSaved) ?: "Error saving player")

                }
            }, {
                //Acción borrar
                val context = requireContext()

                AlertDialog.Builder(requireContext())
                    .setTitle(context?.getString(R.string.textConfirmation) ?: "Confirmation")
                    .setMessage(getString(R.string.confirm_delete_player, player.player))
                    .setPositiveButton(context?.getString(R.string.textAccept) ?: "Accept"){ _, _ ->
                        try {
                            lifecycleScope.launch {

                                val result = async {
                                    repository.deletePlayer(player)
                                }

                                result.await()

                                withContext(Dispatchers.Main){
                                    message(context?.getString(R.string.textPlayerDeleted) ?: "Player deleted successfully")

                                    updateUI()
                                }
                            }

                        }catch(e: IOException) {
                            e.printStackTrace()

                            message(context?.getString(R.string.textErrorPlayerDeleted) ?: "Error deleting player")

                        }
                    }
                    .setNegativeButton(context?.getString(R.string.textCancel) ?: "Cancel"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            })

        return dialog
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se va a llamar después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()


        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        /*binding.tietTitle.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                //Aquí ya perdió el foco esa vista

            }
        }*/

        /*
        binding.tietPlayer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        */

        binding.tietTeam.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietNationality.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

    }

    private fun validateFields(): Boolean {
        val selectedPlayer = binding.spPlayer.selectedItem?.toString() ?: ""
        val team = binding.tietTeam.text.toString()
        val nationality = binding.tietNationality.text.toString()

        return ((selectedPlayer != "Select a player") && team.isNotEmpty() && nationality.isNotEmpty())
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton(btn1Text) { _, _ ->
                //Acción para el botón positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text) { _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()
}
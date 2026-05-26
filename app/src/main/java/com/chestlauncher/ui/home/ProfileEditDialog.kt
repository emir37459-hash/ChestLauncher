package com.chestlauncher.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chestlauncher.R
import com.chestlauncher.data.models.LaunchProfile
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.UUID

class ProfileEditDialog(
    private val profile: LaunchProfile?,
    private val onSave: (LaunchProfile) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_profile_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName    = view.findViewById<EditText>(R.id.etProfileName)
        val etVersion = view.findViewById<EditText>(R.id.etGameVersion)
        val etRamMin  = view.findViewById<EditText>(R.id.etRamMin)
        val etRamMax  = view.findViewById<EditText>(R.id.etRamMax)
        val spinnerJava = view.findViewById<Spinner>(R.id.spinnerJavaVersion)
        val btnSave   = view.findViewById<Button>(R.id.btnSaveProfile)

        val javaOptions = arrayOf("auto", "Java 8", "Java 11", "Java 17", "Java 21", "Custom")
        spinnerJava.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, javaOptions
        )

        profile?.let {
            etName.setText(it.name)
            etVersion.setText(it.gameVersion)
            etRamMin.setText(it.ramMin.toString())
            etRamMax.setText(it.ramMax.toString())
            val javaIdx = javaOptions.indexOf(it.javaVersion).coerceAtLeast(0)
            spinnerJava.setSelection(javaIdx)
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val version = etVersion.text.toString().trim()
            if (name.isEmpty() || version.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val saved = (profile ?: LaunchProfile(UUID.randomUUID().toString(), "", "")).copy(
                name        = name,
                gameVersion = version,
                javaVersion = spinnerJava.selectedItem.toString(),
                ramMin      = etRamMin.text.toString().toIntOrNull() ?: 512,
                ramMax      = etRamMax.text.toString().toIntOrNull() ?: 2048
            )
            onSave(saved)
            dismiss()
        }
    }
}

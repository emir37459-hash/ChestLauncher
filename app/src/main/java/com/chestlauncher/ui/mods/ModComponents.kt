package com.chestlauncher.ui.mods

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chestlauncher.R
import com.chestlauncher.data.models.ModEntry
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.UUID

// ── ModAdapter ────────────────────────────────────────────────────────────────

class ModAdapter(
    private val onToggle: (ModEntry) -> Unit,
    private val onDelete: (ModEntry) -> Unit
) : ListAdapter<ModEntry, ModAdapter.VH>(DIFF) {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvName:    TextView    = v.findViewById(R.id.tvModName)
        val tvVersion: TextView    = v.findViewById(R.id.tvModVersion)
        val swEnabled: Switch      = v.findViewById(R.id.swModEnabled)
        val btnDelete: ImageButton = v.findViewById(R.id.btnDeleteMod)
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_mod, p, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val mod = getItem(pos)
        h.tvName.text    = mod.name
        h.tvVersion.text = mod.version
        h.swEnabled.isChecked = mod.isEnabled
        h.swEnabled.setOnClickListener { onToggle(mod) }
        h.btnDelete.setOnClickListener { onDelete(mod) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ModEntry>() {
            override fun areItemsTheSame(a: ModEntry, b: ModEntry) = a.id == b.id
            override fun areContentsTheSame(a: ModEntry, b: ModEntry) = a == b
        }
    }
}

// ── AddModDialog ──────────────────────────────────────────────────────────────

class AddModDialog(
    private val profileId: String,
    private val onAdd: (ModEntry) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) =
        i.inflate(R.layout.dialog_add_mod, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName    = view.findViewById<EditText>(R.id.etModName)
        val etVersion = view.findViewById<EditText>(R.id.etModVersion)
        val etFile    = view.findViewById<EditText>(R.id.etModFile)
        val btnAdd    = view.findViewById<Button>(R.id.btnAddMod)

        btnAdd.setOnClickListener {
            val name    = etName.text.toString().trim()
            val version = etVersion.text.toString().trim()
            val file    = etFile.text.toString().trim()
            if (name.isEmpty()) { etName.error = "Required"; return@setOnClickListener }

            onAdd(
                ModEntry(
                    id        = UUID.randomUUID().toString(),
                    name      = name,
                    version   = version.ifEmpty { "unknown" },
                    fileName  = file.ifEmpty { "$name.jar" },
                    profileId = profileId
                )
            )
            dismiss()
        }
    }
}

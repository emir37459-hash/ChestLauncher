package com.chestlauncher.ui.accounts

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chestlauncher.R
import com.chestlauncher.data.models.AccountType
import com.chestlauncher.data.models.MinecraftAccount
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// ── AccountAdapter ────────────────────────────────────────────────────────────

class AccountAdapter(
    private val onSelect: (MinecraftAccount) -> Unit,
    private val onDelete: (MinecraftAccount) -> Unit
) : ListAdapter<MinecraftAccount, AccountAdapter.VH>(DIFF) {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon:     ImageView    = view.findViewById(R.id.ivAccountIcon)
        val tvName:     TextView     = view.findViewById(R.id.tvAccountItemName)
        val tvType:     TextView     = view.findViewById(R.id.tvAccountItemType)
        val ivSelected: ImageView    = view.findViewById(R.id.ivAccountSelected)
        val btnDelete:  ImageButton  = view.findViewById(R.id.btnDeleteAccount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val acc = getItem(position)
        holder.tvName.text = acc.username
        holder.tvType.text = if (acc.type == AccountType.MICROSOFT) "Microsoft" else "Offline"
        holder.ivIcon.setImageResource(
            if (acc.type == AccountType.MICROSOFT) R.drawable.ic_microsoft else R.drawable.ic_person
        )
        holder.ivSelected.visibility = if (acc.isSelected) View.VISIBLE else View.INVISIBLE
        holder.itemView.setOnClickListener { onSelect(acc) }
        holder.btnDelete.setOnClickListener { onDelete(acc) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MinecraftAccount>() {
            override fun areItemsTheSame(a: MinecraftAccount, b: MinecraftAccount) = a.uuid == b.uuid
            override fun areContentsTheSame(a: MinecraftAccount, b: MinecraftAccount) = a == b
        }
    }
}

// ── OfflineLoginDialog ────────────────────────────────────────────────────────

class OfflineLoginDialog(private val onAdd: (String) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?) =
        inflater.inflate(R.layout.dialog_offline_login, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val et = view.findViewById<EditText>(R.id.etOfflineUsername)
        view.findViewById<Button>(R.id.btnAddOfflineAccount).setOnClickListener {
            val name = et.text.toString().trim()
            if (name.length < 3) {
                et.error = "Username must be at least 3 characters"
                return@setOnClickListener
            }
            onAdd(name)
            dismiss()
        }
    }
}

// ── MicrosoftLoginDialog ──────────────────────────────────────────────────────

class MicrosoftLoginDialog(private val onToken: (String) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?) =
        inflater.inflate(R.layout.dialog_microsoft_login, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Full OAuth2 flow: open WebView → catch redirect → exchange code → get XSTS → get MC token
        view.findViewById<Button>(R.id.btnStartMSLogin).setOnClickListener {
            // Placeholder – real implementation needs WebView + OAuth redirect
            onToken("placeholder_ms_token")
            dismiss()
        }
    }
}

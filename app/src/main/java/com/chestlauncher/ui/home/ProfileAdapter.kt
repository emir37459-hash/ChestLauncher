package com.chestlauncher.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chestlauncher.R
import com.chestlauncher.data.models.LaunchProfile

class ProfileAdapter(
    private val onSelect: (LaunchProfile) -> Unit,
    private val onEdit:   (LaunchProfile) -> Unit,
    private val onDelete: (LaunchProfile) -> Unit
) : ListAdapter<LaunchProfile, ProfileAdapter.ViewHolder>(DIFF) {

    private var selectedId: String? = null

    fun setSelected(id: String) {
        selectedId = id
        notifyDataSetChanged()
    }

    fun getSelected(): LaunchProfile? = currentList.find { it.id == selectedId }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView       = view.findViewById(R.id.cardProfile)
        val ivIcon: ImageView    = view.findViewById(R.id.ivProfileIcon)
        val tvName: TextView     = view.findViewById(R.id.tvProfileName)
        val tvVersion: TextView  = view.findViewById(R.id.tvProfileVersion)
        val tvJava: TextView     = view.findViewById(R.id.tvProfileJava)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditProfile)
        val btnDel: ImageButton  = view.findViewById(R.id.btnDeleteProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = getItem(position)
        holder.tvName.text    = profile.name
        holder.tvVersion.text = "Minecraft ${profile.gameVersion}"
        holder.tvJava.text    = "Java ${profile.javaVersion}"
        holder.ivIcon.setImageResource(R.drawable.ic_chest)
        holder.card.isSelected = profile.id == selectedId
        holder.card.strokeWidth = if (profile.id == selectedId) 4 else 0

        holder.card.setOnClickListener { onSelect(profile) }
        holder.btnEdit.setOnClickListener { onEdit(profile) }
        holder.btnDel.setOnClickListener { onDelete(profile) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<LaunchProfile>() {
            override fun areItemsTheSame(a: LaunchProfile, b: LaunchProfile) = a.id == b.id
            override fun areContentsTheSame(a: LaunchProfile, b: LaunchProfile) = a == b
        }
    }
}

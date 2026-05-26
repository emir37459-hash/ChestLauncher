package com.chestlauncher.ui.settings

import android.graphics.Color
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chestlauncher.R
import com.chestlauncher.data.models.AppTheme

class ThemeAdapter(
    private var selected: AppTheme,
    private val onSelect: (AppTheme) -> Unit
) : RecyclerView.Adapter<ThemeAdapter.VH>() {

    private val themes = AppTheme.values().toList()

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val card:    CardView  = v.findViewById(R.id.cardTheme)
        val preview: View      = v.findViewById(R.id.viewThemePreview)
        val tvName:  TextView  = v.findViewById(R.id.tvThemeName)
        val ivCheck: ImageView = v.findViewById(R.id.ivThemeCheck)
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_theme, p, false))

    override fun getItemCount() = themes.size

    override fun onBindViewHolder(h: VH, pos: Int) {
        val theme = themes[pos]
        h.tvName.text = theme.displayName
        h.ivCheck.visibility = if (theme == selected) View.VISIBLE else View.GONE

        // Preview color swatch
        val bgColor = when (theme) {
            AppTheme.DARK      -> Color.parseColor("#1a1a2e")
            AppTheme.MINECRAFT -> Color.parseColor("#5a8a3c")
            AppTheme.MODERN    -> Color.parseColor("#f5f5f5")
            AppTheme.COLORFUL  -> Color.parseColor("#ff6b6b")
            AppTheme.AMOLED    -> Color.parseColor("#000000")
            AppTheme.FOREST    -> Color.parseColor("#2d5a27")
            AppTheme.OCEAN     -> Color.parseColor("#1a6b8a")
        }
        h.preview.setBackgroundColor(bgColor)
        // strokeWidth removed

        h.card.setOnClickListener {
            selected = theme
            notifyDataSetChanged()
            onSelect(theme)
        }
    }
}

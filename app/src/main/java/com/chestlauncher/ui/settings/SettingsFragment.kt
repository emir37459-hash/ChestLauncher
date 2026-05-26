package com.chestlauncher.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chestlauncher.data.models.AppTheme
import com.chestlauncher.data.repository.LauncherRepository
import com.chestlauncher.databinding.FragmentSettingsBinding
import com.chestlauncher.ui.MainActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LauncherRepository
    private lateinit var themeAdapter: ThemeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LauncherRepository(requireContext())

        setupThemePicker()
        setupJavaSection()
        setupMiscSettings()
    }

    private fun setupThemePicker() {
        val currentTheme = repository.getTheme()

        themeAdapter = ThemeAdapter(currentTheme) { selected ->
            repository.setTheme(selected)
            (activity as? MainActivity)?.restartWithTheme()
        }

        binding.rvThemes.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = themeAdapter
        }
    }

    private fun setupJavaSection() {
        val installations = repository.getJavaInstallations()
        binding.tvJavaCount.text = "${installations.size} Java installation(s) found"

        binding.btnScanJava.setOnClickListener {
            // TODO: Scan /data/data/com.chestlauncher/files/java and /sdcard directories
            Toast.makeText(context, "Scanning for Java installations...", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddCustomJava.setOnClickListener {
            // TODO: Open file picker to select java binary
            Toast.makeText(context, "Select Java binary from storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupMiscSettings() {
        val settings = repository.getSettings()

        binding.swCloseOnLaunch.isChecked = settings.closeOnLaunch
        binding.swCloseOnLaunch.setOnCheckedChangeListener { _, checked ->
            repository.saveSettings(settings.copy(closeOnLaunch = checked))
        }

        binding.swShowConsole.isChecked = settings.showConsole
        binding.swShowConsole.setOnCheckedChangeListener { _, checked ->
            repository.saveSettings(settings.copy(showConsole = checked))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

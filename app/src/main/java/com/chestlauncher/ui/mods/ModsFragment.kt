package com.chestlauncher.ui.mods

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chestlauncher.data.models.ModEntry
import com.chestlauncher.data.repository.LauncherRepository
import com.chestlauncher.databinding.FragmentModsBinding

class ModsFragment : Fragment() {

    private var _binding: FragmentModsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LauncherRepository
    private lateinit var modAdapter: ModAdapter
    private var currentProfileId: String = "default"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LauncherRepository(requireContext())

        modAdapter = ModAdapter(
            onToggle = { mod ->
                val updated = mod.copy(isEnabled = !mod.isEnabled)
                repository.saveMod(updated)
                loadMods()
            },
            onDelete = { mod ->
                repository.removeMod(mod.id, mod.profileId)
                loadMods()
            }
        )

        binding.rvMods.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = modAdapter
        }

        binding.fabAddMod.setOnClickListener { showAddModDialog() }
        loadMods()
    }

    private fun loadMods() {
        val mods = repository.getMods(currentProfileId)
        modAdapter.submitList(mods)
        binding.tvNoMods.visibility = if (mods.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showAddModDialog() {
        val dialog = AddModDialog(currentProfileId) { mod ->
            repository.saveMod(mod)
            loadMods()
            Toast.makeText(context, "Mod added: ${mod.name}", Toast.LENGTH_SHORT).show()
        }
        dialog.show(parentFragmentManager, "AddMod")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

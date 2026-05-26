package com.chestlauncher.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chestlauncher.R
import com.chestlauncher.data.models.LaunchProfile
import com.chestlauncher.data.repository.LauncherRepository
import com.chestlauncher.databinding.FragmentHomeBinding
import java.util.UUID

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LauncherRepository
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LauncherRepository(requireContext())

        setupProfileList()
        setupAccountCard()
        setupLaunchButton()
        setupAddProfileButton()
    }

    private fun setupProfileList() {
        profileAdapter = ProfileAdapter(
            onSelect = { profile -> onProfileSelected(profile) },
            onEdit   = { profile -> showEditProfileDialog(profile) },
            onDelete = { profile ->
                repository.removeProfile(profile.id)
                loadProfiles()
            }
        )
        binding.rvProfiles.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileAdapter
        }
        loadProfiles()
    }

    private fun loadProfiles() {
        val profiles = repository.getProfiles()
        profileAdapter.submitList(profiles)
        binding.tvNoProfiles.visibility = if (profiles.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupAccountCard() {
        val account = repository.getSelectedAccount()
        if (account != null) {
            binding.tvAccountName.text = account.username
            binding.tvAccountType.text = account.type.name
            binding.ivAccountBadge.setImageResource(
                if (account.type.name == "MICROSOFT") R.drawable.ic_microsoft
                else R.drawable.ic_person
            )
        } else {
            binding.tvAccountName.text = getString(R.string.no_account_selected)
            binding.tvAccountType.text = getString(R.string.tap_to_add)
        }
    }

    private fun setupLaunchButton() {
        binding.btnLaunch.setOnClickListener {
            val account = repository.getSelectedAccount()
            if (account == null) {
                Toast.makeText(context, getString(R.string.select_account_first), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val profile = profileAdapter.getSelected()
            if (profile == null) {
                Toast.makeText(context, getString(R.string.select_profile_first), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            launchGame(account.username, profile)
        }
    }

    private fun setupAddProfileButton() {
        binding.fabAddProfile.setOnClickListener {
            showEditProfileDialog(null)
        }
    }

    private fun onProfileSelected(profile: LaunchProfile) {
        profileAdapter.setSelected(profile.id)
        binding.btnLaunch.isEnabled = true
    }

    private fun showEditProfileDialog(profile: LaunchProfile?) {
        val dialog = ProfileEditDialog(
            profile = profile,
            onSave = { saved ->
                repository.saveProfile(saved)
                loadProfiles()
            }
        )
        dialog.show(parentFragmentManager, "ProfileEdit")
    }

    private fun launchGame(username: String, profile: LaunchProfile) {
        Toast.makeText(
            context,
            "Launching ${profile.name} as $username...",
            Toast.LENGTH_SHORT
        ).show()
        // TODO: Hook into PojavLauncher / FCL core launch intent
    }

    override fun onResume() {
        super.onResume()
        setupAccountCard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

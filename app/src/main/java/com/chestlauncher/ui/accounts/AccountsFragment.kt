package com.chestlauncher.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chestlauncher.data.models.AccountType
import com.chestlauncher.data.models.MinecraftAccount
import com.chestlauncher.data.repository.LauncherRepository
import com.chestlauncher.databinding.FragmentAccountsBinding
import java.util.UUID

class AccountsFragment : Fragment() {

    private var _binding: FragmentAccountsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LauncherRepository
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = LauncherRepository(requireContext())

        accountAdapter = AccountAdapter(
            onSelect = { acc ->
                repository.selectAccount(acc.uuid)
                loadAccounts()
            },
            onDelete = { acc ->
                repository.removeAccount(acc.uuid)
                loadAccounts()
            }
        )
        binding.rvAccounts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = accountAdapter
        }
        loadAccounts()

        binding.btnAddMicrosoft.setOnClickListener { startMicrosoftLogin() }
        binding.btnAddOffline.setOnClickListener   { showOfflineDialog() }
    }

    private fun loadAccounts() {
        val accounts = repository.getAccounts()
        accountAdapter.submitList(accounts)
        binding.tvNoAccounts.visibility = if (accounts.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun startMicrosoftLogin() {
        // TODO: Implement full Microsoft OAuth2 + XSTS + Minecraft auth flow
        // For now show a placeholder
        val dialog = MicrosoftLoginDialog { token ->
            val acc = MinecraftAccount(
                uuid         = UUID.randomUUID().toString(),
                username     = "MSAccount",
                type         = AccountType.MICROSOFT,
                accessToken  = token
            )
            repository.saveAccount(acc)
            loadAccounts()
        }
        dialog.show(parentFragmentManager, "MicrosoftLogin")
    }

    private fun showOfflineDialog() {
        val dialog = OfflineLoginDialog { username ->
            val acc = MinecraftAccount(
                uuid     = UUID.randomUUID().toString(),
                username = username,
                type     = AccountType.OFFLINE
            )
            repository.saveAccount(acc)
            if (repository.getSelectedAccount() == null) {
                repository.selectAccount(acc.uuid)
            }
            loadAccounts()
        }
        dialog.show(parentFragmentManager, "OfflineLogin")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

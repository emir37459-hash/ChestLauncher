package com.chestlauncher.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.chestlauncher.data.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LauncherRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("chest_launcher_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // ── Accounts ─────────────────────────────────────────────────────────────

    fun getAccounts(): List<MinecraftAccount> {
        val json = prefs.getString(KEY_ACCOUNTS, "[]") ?: "[]"
        val type = object : TypeToken<List<MinecraftAccount>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveAccount(account: MinecraftAccount) {
        val accounts = getAccounts().toMutableList()
        val idx = accounts.indexOfFirst { it.uuid == account.uuid }
        if (idx >= 0) accounts[idx] = account else accounts.add(account)
        prefs.edit().putString(KEY_ACCOUNTS, gson.toJson(accounts)).apply()
    }

    fun removeAccount(uuid: String) {
        val accounts = getAccounts().filter { it.uuid != uuid }
        prefs.edit().putString(KEY_ACCOUNTS, gson.toJson(accounts)).apply()
    }

    fun getSelectedAccount(): MinecraftAccount? = getAccounts().find { it.isSelected }

    fun selectAccount(uuid: String) {
        val accounts = getAccounts().map { it.copy(isSelected = it.uuid == uuid) }
        prefs.edit().putString(KEY_ACCOUNTS, gson.toJson(accounts)).apply()
    }

    // ── Profiles ──────────────────────────────────────────────────────────────

    fun getProfiles(): List<LaunchProfile> {
        val json = prefs.getString(KEY_PROFILES, "[]") ?: "[]"
        val type = object : TypeToken<List<LaunchProfile>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveProfile(profile: LaunchProfile) {
        val profiles = getProfiles().toMutableList()
        val idx = profiles.indexOfFirst { it.id == profile.id }
        if (idx >= 0) profiles[idx] = profile else profiles.add(profile)
        prefs.edit().putString(KEY_PROFILES, gson.toJson(profiles)).apply()
    }

    fun removeProfile(id: String) {
        val profiles = getProfiles().filter { it.id != id }
        prefs.edit().putString(KEY_PROFILES, gson.toJson(profiles)).apply()
    }

    // ── Mods ──────────────────────────────────────────────────────────────────

    fun getMods(profileId: String): List<ModEntry> {
        val json = prefs.getString("${KEY_MODS}_$profileId", "[]") ?: "[]"
        val type = object : TypeToken<List<ModEntry>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveMod(mod: ModEntry) {
        val mods = getMods(mod.profileId).toMutableList()
        val idx = mods.indexOfFirst { it.id == mod.id }
        if (idx >= 0) mods[idx] = mod else mods.add(mod)
        prefs.edit().putString("${KEY_MODS}_${mod.profileId}", gson.toJson(mods)).apply()
    }

    fun removeMod(modId: String, profileId: String) {
        val mods = getMods(profileId).filter { it.id != modId }
        prefs.edit().putString("${KEY_MODS}_$profileId", gson.toJson(mods)).apply()
    }

    // ── Java Installations ────────────────────────────────────────────────────

    fun getJavaInstallations(): List<JavaInstallation> {
        val json = prefs.getString(KEY_JAVA, "[]") ?: "[]"
        val type = object : TypeToken<List<JavaInstallation>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveJavaInstallation(java: JavaInstallation) {
        val list = getJavaInstallations().toMutableList()
        val idx = list.indexOfFirst { it.path == java.path }
        if (idx >= 0) list[idx] = java else list.add(java)
        prefs.edit().putString(KEY_JAVA, gson.toJson(list)).apply()
    }

    // ── Settings ──────────────────────────────────────────────────────────────

    fun getSettings(): AppSettings {
        val json = prefs.getString(KEY_SETTINGS, null) ?: return AppSettings()
        return try { gson.fromJson(json, AppSettings::class.java) } catch (e: Exception) { AppSettings() }
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit().putString(KEY_SETTINGS, gson.toJson(settings)).apply()
    }

    fun getTheme(): AppTheme = getSettings().theme

    fun setTheme(theme: AppTheme) {
        saveSettings(getSettings().copy(theme = theme))
    }

    companion object {
        private const val KEY_ACCOUNTS = "accounts"
        private const val KEY_PROFILES = "profiles"
        private const val KEY_MODS     = "mods"
        private const val KEY_JAVA     = "java_installations"
        private const val KEY_SETTINGS = "app_settings"
    }
}

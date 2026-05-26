package com.chestlauncher.data.models

import com.google.gson.annotations.SerializedName

// ── Account ──────────────────────────────────────────────────────────────────

enum class AccountType { MICROSOFT, OFFLINE }

data class MinecraftAccount(
    val uuid: String,
    val username: String,
    val type: AccountType,
    val accessToken: String = "",
    val refreshToken: String = "",
    val skinUrl: String = "",
    val isSelected: Boolean = false
)

// ── Profile ───────────────────────────────────────────────────────────────────

data class LaunchProfile(
    val id: String,
    val name: String,
    val gameVersion: String,
    val javaVersion: String = "auto",
    val customJavaPath: String = "",
    val ramMin: Int = 512,
    val ramMax: Int = 2048,
    val extraJvmArgs: String = "",
    val lastUsed: Long = 0L,
    val iconName: String = "chest"
)

// ── Mod ───────────────────────────────────────────────────────────────────────

enum class ModLoader { FORGE, FABRIC, QUILT, NEOFORGE, VANILLA }

data class ModEntry(
    val id: String,
    val name: String,
    val version: String,
    val fileName: String,
    val isEnabled: Boolean = true,
    val profileId: String,
    val downloadUrl: String = "",
    val description: String = "",
    val iconUrl: String = ""
)

// ── Java Version ──────────────────────────────────────────────────────────────

data class JavaInstallation(
    val version: String,
    val path: String,
    val isEmbedded: Boolean = false,
    val architecture: String = "aarch64"
)

// ── Theme ─────────────────────────────────────────────────────────────────────

enum class AppTheme(val displayName: String, val styleRes: String) {
    DARK("Dark Mode", "Theme.ChestLauncher.Dark"),
    MINECRAFT("Minecraft", "Theme.ChestLauncher.Minecraft"),
    MODERN("Modern / Minimal", "Theme.ChestLauncher.Modern"),
    COLORFUL("Colorful", "Theme.ChestLauncher.Colorful"),
    AMOLED("AMOLED Black", "Theme.ChestLauncher.Amoled"),
    FOREST("Forest Green", "Theme.ChestLauncher.Forest"),
    OCEAN("Ocean Blue", "Theme.ChestLauncher.Ocean")
}

// ── App Settings ──────────────────────────────────────────────────────────────

data class AppSettings(
    val theme: AppTheme = AppTheme.DARK,
    val language: String = "system",
    val gameDirectory: String = "",
    val closeOnLaunch: Boolean = false,
    val showConsole: Boolean = false,
    val analyticsEnabled: Boolean = false
)

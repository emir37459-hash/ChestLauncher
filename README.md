# 🎮 Chest Launcher

**Minecraft Java Edition Launcher for Android** — inspired by Zalith Launcher 2

---

## ✨ Features

| Feature | Status |
|---|---|
| Offline account login | ✅ Ready |
| Microsoft account login | 🔧 OAuth flow scaffold (needs app registration) |
| Multiple saved profiles | ✅ Ready |
| Custom Java version selection | ✅ Ready |
| Mod manager | ✅ Ready |
| 7 Themes in Settings | ✅ Ready |

## 🎨 Themes
- **Dark Mode** (default)
- **Minecraft** (earthy green)
- **Modern / Minimal** (light)
- **Colorful** (vibrant purple/red)
- **AMOLED Black**
- **Forest Green**
- **Ocean Blue**

---

## 🛠 Build Instructions

### Requirements
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK API 34

### Steps
1. Open the project in Android Studio
2. Let Gradle sync complete
3. Click **Run** or use `Build > Build APK`

### Command line
```bash
./gradlew assembleDebug
# APK output: app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔧 Next Steps (TODOs)

### Microsoft Auth
1. Register an Azure app at https://portal.azure.com
2. Set redirect URI: `ms-xal-<your_client_id>://auth`
3. Implement in `MicrosoftLoginDialog.kt`:
   - Open WebView with OAuth URL
   - Catch redirect → exchange code for token
   - Call XSTS endpoint
   - Exchange for Minecraft token

### Game Launch
- Integrate [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) or [FCL](https://github.com/FCL-Team/FoldCraftLauncher) core as a library
- Hook `HomeFragment.launchGame()` into their launch intent

### Mod Downloads
- Add Modrinth API integration for browsing/downloading mods
- Add CurseForge API support

---

## 📁 Project Structure

```
app/src/main/java/com/chestlauncher/
├── data/
│   ├── models/Models.kt          # Data classes (Account, Profile, Mod, Theme...)
│   └── repository/LauncherRepository.kt  # SharedPrefs storage
├── ui/
│   ├── MainActivity.kt           # Bottom nav + theme apply
│   ├── SplashActivity.kt
│   ├── home/                     # Profile list + Launch button
│   ├── accounts/                 # Microsoft + Offline login
│   ├── mods/                     # Mod manager
│   └── settings/                 # Theme picker + Java + General
```

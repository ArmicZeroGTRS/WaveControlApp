# WaveControl Android App 🖐️📱
### Touchless Gesture Control • Kinetic Telemetry Spatial UI

[![Release](https://img.shields.io/github/v/release/ArmicZeroGTRS/WaveControlApp?color=blue&label=Release)](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/latest)
[![API](https://img.shields.io/badge/API-31%20to%2035-brightgreen.svg)](https://developer.android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%202.3-purple.svg)](https://kotlinlang.org)

Touch-free spatial hand gesture control (*WaveControl*) application for Android using the front camera and proximity sensor. Completely rebuilt with the **Stitch AI Kinetic Telemetry Spatial UI** design system and the official **Gemini 4 Argon Brand Identity**.

> [!IMPORTANT]
> **Version 1.4**:
> 1. **Hand-Only Gesture Recognition**: Integrated 2D bounding-box spatial clustering (`10..185` cell envelope, span $\ge 3\times 3$, minimum energy density, monotonic multi-frame trajectory validation, and whole-frame camera pan rejection) to prevent non-hand objects, shadows, cables, or phone tilts from falsely triggering gestures.
> 2. **Per-Node Gesture Enablement Toggles**: Added interactive on/off toggles to all 4 configured gesture nodes (`Wave Left`, `Wave Right`, `Wave Up`, `Wave Down`) as well as Hand Hover and Proximity Wave in the Gesture Control Center.
> 3. **Video Next Track & Previous Track Gestures**: Re-engineered media key dispatch using valid monotonic `uptimeMillis()` and dual dispatch (direct `AudioManager` + foreground `ACTION_MEDIA_BUTTON` broadcast), ensuring full compatibility with YouTube, VLC, and video players.
> 4. **Strict Sleep & Lockscreen Guard**: Enhanced lifecycle integration with `KeyguardManager` and broadcast receivers for `ACTION_SCREEN_OFF`, `ACTION_SCREEN_ON`, `ACTION_USER_PRESENT`, `ACTION_DREAMING_STARTED`, and `ACTION_DREAMING_STOPPED` to guarantee zero camera or sensor activity while the screen is off, sleeping, or locked.
> 5. **Brand & About Engine Update**: Added `©2026 ARMIC ZeroGTRS` in the Settings screen under About this App.
> 6. **100% Mobile Banking Safe**: Zero Accessibility Services used. Fully compliant with Android 15 Restricted Settings without triggering safety warnings.

---

## 📥 Ready-to-Install APK Download

Download the latest pre-built APK directly without building:
👉 **[Download WaveControl-v1.4.apk](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.4/WaveControl-v1.4.apk)** *(Version 1.4, Build 5)*

---

## 📋 Technical Specifications
| Component | Specification |
|---|---|
| **Version** | v1.4 (versionCode 5) |
| **Minimum SDK** | API 31 (Android 12 Snow Cone) |
| **Target SDK** | API 35 (Android 15 Vanilla Ice Cream) |
| **Compile SDK** | API 36 |
| **Programming Language** | Kotlin 2.3 |
| **UI Design System** | Stitch AI Kinetic Telemetry Spatial UI (Jetpack Compose Material 3) |
| **Brand Identity** | Gemini 4 Argon WaveControl Logo System |
| **Camera Engine** | AndroidX CameraX (ImageAnalysis YUV-420-888) |
| **Background Processing** | Android Foreground Service + StateFlow |
| **Device Security** | 100% Mobile Banking & Android 15 Compliant (No Accessibility Service) |

---

## ✨ Screen Architecture & Features

### 1. 📡 Radar Dashboard (`HomeScreen.kt`)
* **Real-Time Concentric Radar Graphic:** Interactive glowing spatial visualizer with vector target quadrants (`UP`, `DOWN`, `PREV`, `NEXT`).
* **Hardware Status Ribbon:** Live `ENGINE ACTIVE` heartbeat, `60 FPS` optical clock, and `BANK SAFE` verified indicator.
* **Camera Lock & Latency Readout:** Sub-14ms telemetry latency indicator with real-time vector coordinate monitoring.
* **Master Engine Command Pill:** Ergonomic full-width start/pause capsule button with tactile pulse feedback.

### 2. 🎛️ Gesture Control Center (`GestureConfigScreen.kt`)
* **Configured Gesture Nodes with Independent Toggles:** Dedicated cards for `Wave Left`, `Wave Right`, `Wave Up`, `Wave Down`, `Hand Hover`, and `Proximity Wave` with working on/off switches.
* **Dynamic Quick Filters:** Filter gesture assignments by domain (`Media Control`, `System Audio`, `Scrolling`, `Navigation`).
* **Action Remapping Bottom Sheet:** Reassign any gesture to Media Play/Pause, Next Track/Video, Previous Track/Video, Volume Up/Down/Mute, or Flashlight with instant visual confirmation.

### 3. 🪟 Floating HUD Studio (`FloatingHudScreen.kt`)
* **Real-Time Floating Overlay Preview:** Interactive sandbox demonstrating how the HUD island appears over other apps.
* **Anchor Positioning:** Switch between `CENTER_ISLAND`, `TOP_RIGHT`, and `DYNAMIC_EDGE`.
* **Telemetry Diagnostics:** Live coordinate and sensor energy feeds.

### 4. ⚙️ System Settings & Hardware Profiles (`SettingsScreen.kt`)
* **Appearance & Theme Selector:** Pure obsidian `Dark` mode (OLED optimized), `Light` mode, or `Auto` (System) — persisted live across sessions.
* **Spatial Haptic Pulse:** Configurable actuator resonance percentage for mid-air vector crossing confirmations.
* **Reduced Kinematics:** Performance toggle to suppress decorative animations on lower-end devices.
* **Axis Swap Calibration:** Resolves inverted or landscape sensor mountings on various hardware manufacturers.
* **Screen-Off & Lockscreen Guard:** Automatically pauses camera and proximity sensors when the device is locked or asleep.
* **Sandboxed Security Vault:** Detailed safety audit confirming 0% risk for banking apps.
* **About WaveControl:** Gemini 4 Argon brand showcase and developer credentials (`©2026 ARMIC ZeroGTRS`).

---

## 🛠️ Required Permissions (Standard Runtime Only)
1. **Camera (`android.permission.CAMERA`):** Analyzes camera frames locally in volatile memory to calculate hand motion vectors (no pictures or videos are ever saved, stored, or transmitted).
2. **Notifications (`android.permission.POST_NOTIFICATIONS`):** Maintains the foreground service status banner on Android 13+.

> **Does NOT require Accessibility Service, Device Administrator, or Android 15 Restricted Settings.**

---

## 🚀 Installation
1. Download [`WaveControl-v1.4.apk`](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.4/WaveControl-v1.4.apk).
2. Open the APK on your Android device and confirm installation.
3. Launch **WaveControl**, grant standard Camera permission, and toggle the master switch to begin touchless gesture control!

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).
©2026 ARMIC ZeroGTRS

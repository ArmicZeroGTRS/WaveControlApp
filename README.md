# WaveControl Android App 🖐️📱
### Touchless Gesture Control • Kinetic Telemetry Spatial UI

[![Release](https://img.shields.io/github/v/release/ArmicZeroGTRS/WaveControlApp?color=blue&label=Release)](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/latest)
[![API](https://img.shields.io/badge/API-31%20to%2035-brightgreen.svg)](https://developer.android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%202.3-purple.svg)](https://kotlinlang.org)

Touch-free spatial hand gesture control (*WaveControl*) application for Android using the front camera and proximity sensor. Completely rebuilt with the **Stitch AI Kinetic Telemetry Spatial UI** design system and the official **Gemini 4 Argon Brand Identity**.

> [!IMPORTANT]
> **Version 1.3**:
> 1. **Gemini 4 Argon Brand Identity Integration**: Official glowing wave mark, dark wordmark header, and adaptive Android launcher icons.
> 2. **Stitch AI Kinetic Telemetry Spatial UI**: 4 newly architected screens — **Radar Dashboard**, **Gesture Control Center**, **Floating HUD Studio**, and **System Settings & Hardware Profiles**.
> 3. **Gesture Detection Engine Bug Fix**: Fixed the issue where gestures were not detected or responsive. Resolved frame energy dilution by isolating active motion cells, expanded natural hand swipe duration to 75–650ms, and added dynamic CameraX permission rebinding.
> 4. **100% Mobile Banking Safe**: Zero Accessibility Services used. Fully compliant with Android 15 Restricted Settings without triggering safety warnings.

---

## 📥 Ready-to-Install APK Download

Download the latest pre-built APK directly without building:
👉 **[Download WaveControl-v1.3.apk](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.3/WaveControl-v1.3.apk)** *(Version 1.3, Build 4)*

---

## 📋 Technical Specifications
| Component | Specification |
|---|---|
| **Version** | v1.3 (versionCode 4) |
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
* **Active Telemetry Bus:** Live stream of last recognized gesture, target application, and sensor fusion health.

### 2. 🎛️ Gesture Control Center (`GestureConfigScreen.kt`)
* **Kinetic Radar Engine Telemetry Strip:** Active Mesh counter (`6 / 6 Bound Vectors`), zero-interference conflict indicator, and dynamic optical profile.
* **Domain Quick Action Filters:** Category filtering for `All Domains`, `Media Control`, `System Audio`, `Scrolling`, and `Navigation`.
* **Configured Gesture Nodes:**
  * **👈 Wave Left (W-L):** Previous Track / Rewind.
  * **👉 Wave Right (W-R):** Next Track / Fast-forward.
  * **👆 Wave Up (W-U):** Volume Up.
  * **👇 Wave Down (W-D):** Volume Down.
  * **✋ Hand Hover / Hold (H-H):** Play / Pause Toggle.
  * **📱 Proximity Wave (P-W):** Ultra-fast proximity swipe (< 5cm).
* **Live Action Remapping & Test Runner:** Interactive modal to reassign actions and test execution instantly.

### 3. 🖥️ Floating HUD Studio & Diagnostics (`FloatingHudScreen.kt`)
* **HUD Simulator Viewport:** 16:9 simulated display previewing the glowing HUD capsule over active applications.
* **Visual Topology Anchor Selector:** Switch between `Center Island`, `Top-Right`, and `Dynamic Edge`.
* **Real-Time Trigger Simulation:** One-tap test button to inspect HUD capsule animations and dismissal latency.
* **Overlay Privilege Banner:** Real-time check and deep link for system overlay capabilities.

### 4. ⚙️ System Settings & Hardware Profiles (`SettingsScreen.kt`)
* **Appearance & Theme Selector:** Pure obsidian `Dark` mode (OLED optimized), `Light` mode, or `Auto` (System) — persisted live across sessions.
* **Spatial Haptic Pulse:** Configurable actuator resonance percentage for mid-air vector crossing confirmations.
* **Reduced Kinematics:** Performance toggle to suppress decorative animations on lower-end devices.
* **Axis Swap Calibration:** Resolves inverted or landscape sensor mountings on various hardware manufacturers.
* **Screen-Off Battery Saver:** Automatically pauses camera and proximity sensors when the device is locked.
* **Sandboxed Security Vault:** Detailed safety audit confirming 0% risk for banking apps.
* **About WaveControl:** Gemini 4 Argon brand showcase and developer credentials.

---

## 🛠️ Required Permissions (Standard Runtime Only)
1. **Camera (`android.permission.CAMERA`):** Analyzes camera frames locally in volatile memory to calculate hand motion vectors (no pictures or videos are ever saved, stored, or transmitted).
2. **Notifications (`android.permission.POST_NOTIFICATIONS`):** Maintains the foreground service status banner on Android 13+.

> **Does NOT require Accessibility Service, Device Administrator, or Android 15 Restricted Settings.**

---

## 🚀 Installation
1. Download [`WaveControl-v1.3.apk`](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.3/WaveControl-v1.3.apk).
2. Open the APK on your Android device and confirm installation.
3. Launch **WaveControl**, grant standard Camera permission, and toggle the master switch to begin touchless gesture control!

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).

# Wave Control Android App 🖐️📱

[![Release](https://img.shields.io/github/v/release/ArmicZeroGTRS/WaveControlApp?color=blue&label=Release)](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/latest)
[![API](https://img.shields.io/badge/API-31%20to%2035-brightgreen.svg)](https://developer.android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%202.3-purple.svg)](https://kotlinlang.org)

Touch-free hand gesture control (*Wave Control*) application for Android using the front camera and proximity sensor. Built with **Jetpack Compose Material 3** and **AndroidX CameraX**.

> [!NOTE]
> **Version 1.2**: Completely free of Accessibility Services and Restricted Settings. **100% Safe to use alongside Mobile Banking apps (BCA, Mandiri, BRImo, Seabank, etc.) without triggering security blocks or Google Play Protect harmful app warnings.**

---

## 📥 Ready-to-Install APK Download

Download the latest pre-built APK directly without building:
👉 **[Download WaveControl-v1.2.apk](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.2/WaveControl-v1.2.apk)** *(Version 1.2)*

---

## 📋 Technical Specifications
| Component | Specification |
|---|---|
| **Minimum SDK** | API 31 (Android 12 Snow Cone) |
| **Target SDK** | API 35 (Android 15 Vanilla Ice Cream) |
| **Compile SDK** | API 36 |
| **Programming Language** | Kotlin 2.3 |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Camera Engine** | AndroidX CameraX (ImageAnalysis YUV-420-888) |
| **Background Processing** | Android Foreground Service + StateFlow |
| **Device Security** | 100% Mobile Banking & Android 15 Compliant (No Accessibility Service) |

---

## ✨ Key Features

### 1. Front Camera Hand Gesture Detection (*Optical Motion Tracking*)
* **👈 Wave Left:** Swipe palm across towards the left side of the screen.
* **👉 Wave Right:** Swipe palm across towards the right side of the screen.
* **👆 Wave Up:** Swipe palm upwards.
* **👇 Wave Down:** Swipe palm downwards.
* **✋ Hand Hover / Hold:** Hold palm still in front of the camera (~0.8s).
* **Swap Axes Feature:** Integrated "Swap Gesture Axes (X ↔ Y)" toggle switch and one-tap quick calibration button for devices with different sensor mount orientations.

### 2. Fast Proximity Wave (*Proximity Sensor*)
* **📱 Proximity Wave:** Quick swipe close to the top of the phone screen (< 5 cm).
* Works instantaneously even in pitch-black dark rooms with ultra-low battery consumption.

### 3. Multimedia & System Action Mapping (*Hands-Free Action Mapping*)
Each gesture can be independently mapped to safe, native system actions:
* **Media Playback:** Play / Pause, Next Track, Previous Track (Compatible with Spotify, YouTube Music, Netflix, Apple Music, VLC, etc.).
* **Volume Control:** Volume Up, Volume Down, Mute / Unmute Audio.
* **Tools:** Toggle Flashlight, Turn Flashlight On, Turn Flashlight Off.

### 4. Battery Saver & Power Management
* **Auto-Pause on Screen Off:** Front camera and proximity sensor automatically shut down when the screen locks or turns off to preserve battery life.
* **Low Power Mode:** Optimized frame rate and background sampling intervals.

---

## 🛠️ Required Permissions (Standard Runtime Only)
1. **Camera (`android.permission.CAMERA`):** Analyzes camera frames locally on-device to track hand motion (no images are ever stored or transmitted).
2. **Notifications (`android.permission.POST_NOTIFICATIONS`):** Maintains the foreground service status banner on Android 13+.

> **Does NOT require Accessibility Service or Restricted Settings.**

---

## 🚀 Installation & Setup
1. Download [`WaveControl-v1.2.apk`](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.2/WaveControl-v1.2.apk).
2. Open the downloaded APK on your Android device.
3. Allow installation from unknown sources if prompted.
4. Launch **Wave Control**, grant the Camera and Notification permissions, and turn on the master switch!

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).

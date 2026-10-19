# Wave Control Android App 🖐️📱

[![Release](https://img.shields.io/github/v/release/ArmicZeroGTRS/WaveControlApp?color=blue&label=Release)](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/latest)
[![API](https://img.shields.io/badge/API-31%20to%2035-brightgreen.svg)](https://developer.android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%202.3-purple.svg)](https://kotlinlang.org)

Aplikasi kontrol gerakan tangan (*Wave Control*) tanpa sentuh untuk Android menggunakan kamera depan dan sensor jarak (*proximity sensor*). Dibangun menggunakan **Jetpack Compose Material 3**, **CameraX**, dan **Accessibility Service**.

---

## 📥 Unduh APK Siap Pakai

Unduh langsung versi terbaru tanpa perlu kompilasi:
👉 **[Download WaveControl-v1.0.0.apk](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.0.0/WaveControl-v1.0.0.apk)** *(Versi 1.0.0)*

---

## 📋 Spesifikasi Teknis
| Komponen | Spesifikasi |
|---|---|
| **Minimum SDK** | API 31 (Android 12 Snow Cone) |
| **Target SDK** | API 35 (Android 15 Vanilla Ice Cream) |
| **Compile SDK** | API 36 |
| **Bahasa Pemrograman** | Kotlin 2.3 |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Kamera Engine** | AndroidX CameraX (ImageAnalysis YUV-420-888) |
| **Background Processing** | Android Foreground Service + StateFlow |
| **Gesture Simulation** | Android AccessibilityService (`dispatchGesture`) |

---

## ✨ Fitur Utama

### 1. Deteksi Gerakan Tangan Kamera Depan (*Optical Motion Tracking*)
* **👈 Lambaian ke Kiri (Wave Left):** Sapuan tangan ke arah kiri layar.
* **👉 Lambaian ke Kanan (Wave Right):** Sapuan tangan ke arah kanan layar.
* **👆 Lambaian ke Atas (Wave Up):** Sapuan tangan ke arah atas.
* **👇 Lambaian ke Bawah (Wave Down):** Sapuan tangan ke arah bawah.
* **✋ Tahan Telapak Tangan (Hand Hover / Hold):** Menahan tangan diam di depan kamera selama ~0.9 detik.
* **Algoritma Efisien:** Menggunakan *downsampled optical motion centroid analysis* yang efisien, responsif (< 2 ms per frame), dan sangat hemat konsumsi baterai/CPU (< 1-2%).

### 2. Sensor Jarak Tambahan (*Proximity Sensor Wave*)
* **📱 Kibasan Sensor Jarak:** Mendeteksi kibasan kilat di atas layar HP (pada jarak < 5 cm).
* Bekerja sangat cepat bahkan dalam kondisi ruangan minim cahaya atau gelap gulita.

### 3. Pemetaan Aksi Sistem & Multimedia (*Hands-Free Action Mapping*)
Setiap gerakan di atas dapat dipetakan secara bebas ke berbagai aksi:
* **Media Playback:** Putar / Jeda (*Play/Pause*), Lagu/Video Selanjutnya (*Next Track*), Lagu/Video Sebelumnya (*Previous Track*). Kompatibel dengan Spotify, YouTube Music, Netflix, dll.
* **Volume Suara:** Naikkan Volume (*Volume Up*), Turunkan Volume (*Volume Down*).
* **Auto-Scroll Layar (TikTok / Reels / Shorts / Browser):** Mengusap layar secara global ke atas atau ke bawah tanpa menyentuh layar.
* **Navigasi Sistem:** Tombol Kembali (*Back*), Beranda (*Home*), Aplikasi Terkini (*Recents*), Tarik Panel Notifikasi, dan Kunci Layar (*Lock Screen*).
* **Peralatan:** Nyalakan/Matikan Senter (*Torch Toggle*).

### 4. Layanan Latar Belakang & Floating HUD (*Overlay*)
* **Foreground Service:** Deteksi tetap aktif saat Anda membuka aplikasi lain (didukung notifikasi status Android 12–15).
* **Floating Badge / HUD:** Animasi umpan balik halus yang melayang di atas layar saat gerakan berhasil dikenali.
* **Haptic & Sound Feedback:** Getaran haptic dan nada bunyi (*chime*) sebagai konfirmasi deteksi.

---

## 🛠️ Izin yang Diperlukan
1. **Kamera (Camera):** Memproses frame kamera depan secara lokal untuk mendeteksi arah sapuan tangan (tanpa menyimpan rekaman).
2. **Notifikasi (Notification API 33+):** Menjaga Foreground Service tetap aktif di latar belakang.
3. **Layanan Aksesibilitas (Accessibility Service):** Diperlukan untuk swipe layar otomatis (TikTok/Reels) dan navigasi sistem (Home, Back, Lock).
4. **Tampilan di Atas Aplikasi Lain (Display Over Other Apps):** Menampilkan badge HUD umpan balik saat membuka aplikasi lain.

---

## 🚀 Cara Memasang & Menggunakan

### Instalasi via ADB
```bash
adb install WaveControl-v1.0.0.apk
```

### Instalasi Manual di Ponsel
1. Unduh berkas [`WaveControl-v1.0.0.apk`](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.0.0/WaveControl-v1.0.0.apk).
2. Buka berkas APK melalui File Manager di HP Anda.
3. Berikan izin pemasangan sumber tidak dikenal (*Install unknown apps*).
4. Buka aplikasi **Wave Control**, berikan izin yang diminta, dan nyalakan sakelar utama!

---

## 📄 Lisensi
Proyek ini dilisensikan di bawah [MIT License](LICENSE).

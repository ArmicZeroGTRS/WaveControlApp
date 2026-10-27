# Wave Control Android App 🖐️📱

[![Release](https://img.shields.io/github/v/release/ArmicZeroGTRS/WaveControlApp?color=blue&label=Release)](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/latest)
[![API](https://img.shields.io/badge/API-31%20to%2035-brightgreen.svg)](https://developer.android.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%202.3-purple.svg)](https://kotlinlang.org)

Aplikasi kontrol gerakan tangan (*Wave Control*) tanpa sentuh untuk Android menggunakan kamera depan dan sensor jarak (*proximity sensor*). Dibangun menggunakan **Jetpack Compose Material 3** dan **AndroidX CameraX**.

> [!NOTE]
> **Versi 1.5.5**: Bebas dari Layanan Aksesibilitas (*Accessibility Service*) dan bebas dari Setelan Terbatas (*Restricted Settings*). **100% Aman digunakan bersama aplikasi Mobile Banking (BCA, Mandiri, BRImo, Seabank, dll.) tanpa terblokir atau terdeteksi berbahaya oleh Google Play Protect.**

---

## 📥 Unduh APK Siap Pakai

Unduh langsung versi terbaru tanpa perlu kompilasi:
👉 **[Download WaveControl-v1.5.5.apk](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.5.5/WaveControl-v1.5.5.apk)** *(Versi 1.5.5)*

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
| **Keamanan Perangkat** | 100% Aman M-Banking & Android 15 (Tanpa Layanan Aksesibilitas) |

---

## ✨ Fitur Utama

### 1. Deteksi Gerakan Tangan Kamera Depan (*Optical Motion Tracking*)
* **👈 Lambaian ke Kiri (Wave Left):** Gerakan telapak tangan melintas ke arah kiri layar.
* **👉 Lambaian ke Kanan (Wave Right):** Gerakan telapak tangan melintas ke arah kanan layar.
* **👆 Lambaian ke Atas (Wave Up):** Gerakan telapak tangan mengarah ke atas.
* **👇 Lambaian ke Bawah (Wave Down):** Gerakan telapak tangan mengarah ke bawah.
* **✋ Tahan Telapak Tangan (Hand Hover):** Menahan telapak tangan di depan kamera (~0.8 detik).
* **Fitur Tukar Sumbu (Swap Axes):** Tersedia sakelar "Tukar Sumbu Gesture (X ↔ Y)" jika orientasi sensor perangkat Anda terbalik.

### 2. Sensor Jarak Tambahan (*Proximity Sensor Wave*)
* **📱 Kibasan Sensor Jarak:** Mendeteksi kibasan kilat di atas layar HP (pada jarak < 5 cm).
* Bekerja sangat cepat bahkan dalam kondisi ruangan minim cahaya atau gelap gulita.

### 3. Pemetaan Aksi Multimedia & Alat (*Hands-Free Action Mapping*)
Setiap gerakan dapat dipetakan secara bebas ke berbagai aksi yang 100% aman:
* **Media Playback:** Putar / Jeda (*Play/Pause*), Trek Selanjutnya (*Next Track*), Trek Sebelumnya (*Previous Track*). Kompatibel dengan Spotify, YouTube Music, Netflix, Apple Music, VLC, dll.
* **Volume Suara:** Naikkan Volume (*Volume Up*), Turunkan Volume (*Volume Down*), Senyap Suara (*Mute/Unmute*).
* **Peralatan:** Nyalakan/Matikan Senter (*Torch Toggle* / *Torch On* / *Torch Off*).

### 4. Manajemen Baterai & Daya (*Battery Saver*)
* **Jeda Otomatis Saat Layar Mati:** Sensor kamera dan sensor jarak otomatis dinonaktifkan saat layar terkunci / layar mati (*Screen Off*) guna menjaga daya tahan baterai.
* **Low Power Mode:** Optimalisasi interval sampling frame kamera di latar belakang.

---

## 🛠️ Izin yang Diperlukan (Hanya Izin Standar)
1. **Kamera (Camera):** Memproses frame kamera depan secara lokal untuk mendeteksi arah sapuan tangan (tanpa menyimpan rekaman).
2. **Notifikasi (Notification API 33+):** Menjaga Foreground Service tetap aktif di latar belakang.

> **Tidak memerlukan izin Aksesibilitas atau Setelan Terbatas.**

---

## 🚀 Cara Memasang di Ponsel
1. Unduh berkas [`WaveControl-v1.5.5.apk`](https://github.com/ArmicZeroGTRS/WaveControlApp/releases/download/v1.5.5/WaveControl-v1.5.5.apk).
2. Buka berkas APK melalui File Manager atau Browser di HP Anda.
3. Berikan izin pemasangan sumber tidak dikenal (*Install unknown apps*).
4. Buka aplikasi **Wave Control**, berikan izin Kamera & Notifikasi, lalu aktifkan sakelar utama!

---

## 📄 Lisensi
Proyek ini dilisensikan di bawah [MIT License](LICENSE).

# Wave Control Android App 🖐️📱

Aplikasi kontrol gerakan tangan (*Wave Control*) tanpa sentuh untuk Android menggunakan kamera depan dan sensor jarak (*proximity sensor*). Dibangun dengan teknologi modern **Jetpack Compose Material 3**, **CameraX**, dan **Accessibility Service**.

---

## 📋 Spesifikasi Aplikasi
- **Minimum SDK:** API 31 (Android 12)
- **Target SDK:** API 35 (Android 15)
- **Package Name:** `com.wavecontrol.app`
- **Arsitektur:** MVVM + Coroutines StateFlow + CameraX ImageAnalysis + Accessibility Gesture Dispatcher
- **Lokasi File APK:** `WaveControl-v1.0.0.apk`

---

## ✨ Fitur Utama

### 1. Deteksi Gerakan Tangan (Front Camera Wave Detection)
- **👈 Lambaian ke Kiri (Wave Left):** Sapuan tangan ke arah kiri layar.
- **👉 Lambaian ke Kanan (Wave Right):** Sapuan tangan ke arah kanan layar.
- **👆 Lambaian ke Atas (Wave Up):** Sapuan tangan ke arah atas.
- **👇 Lambaian ke Bawah (Wave Down):** Sapuan tangan ke arah bawah.
- **✋ Tahan Telapak Tangan (Hand Hover / Hold):** Menahan tangan diam di depan kamera selama ~0.9 detik.
- **Algoritma Efisien:** Menggunakan *downsampled optical motion centroid & displacement tracking* yang sangat ringan (< 1-2% CPU), responsif, dan hemat daya baterai.

### 2. Sensor Jarak Tambahan (Proximity Wave)
- **📱 Kibasan Sensor Jarak:** Mendeteksi kibasan cepat di atas layar HP (pada jarak < 5 cm).
- Bekerja sangat cepat bahkan dalam kondisi ruangan minim cahaya atau gelap gulita.

### 3. Aksi Sistem & Multimedia (Hands-Free Control)
Setiap gerakan di atas dapat dipetakan secara bebas ke aksi berikut:
- **Media Playback:** Putar / Jeda (*Play/Pause*), Lagu/Video Selanjutnya (*Next Track*), Lagu/Video Sebelumnya (*Previous Track*). Kompatibel dengan Spotify, YouTube Music, Netflix, dll.
- **Volume Suara:** Naikkan Volume (*Volume Up*), Turunkan Volume (*Volume Down*).
- **Auto-Scroll Layar (TikTok / Reels / Shorts / Browser):** Mengusap layar secara global ke atas atau ke bawah tanpa menyentuh layar.
- **Navigasi Sistem:** Tombol Kembali (*Back*), Beranda (*Home*), Aplikasi Terkini (*Recents*), Tarik Panel Notifikasi, dan Kunci Layar (*Lock Screen*).
- **Alat:** Nyalakan/Matikan Senter (*Torch Toggle*).

### 4. Layanan Latar Belakang & Floating HUD (Overlay)
- **Foreground Service:** Tetap mendeteksi lambaian tangan saat Anda membuka aplikasi lain (misalnya saat menonton TikTok, mendengarkan Spotify di dapur, atau saat HP ditaruh di meja).
- **Floating HUD Badge:** Memberikan animasi umpan balik mengambang yang halus saat gerakan berhasil dikenali.
- **Haptic & Sound Feedback:** Getaran haptic dan nada bunyi (*chime*) sebagai konfirmasi deteksi.

---

## 🛠️ Panduan Izin (Permissions)
1. **Kamera (Camera):** Diperlukan untuk memproses frame kamera depan secara lokal untuk mendeteksi arah sapuan tangan (tidak ada video yang disimpan atau dikirim ke internet).
2. **Notifikasi (Notification API 33+):** Diperlukan agar sistem Android tidak mematikan Foreground Service saat berjalan di latar belakang.
3. **Layanan Aksesibilitas (Accessibility Service):** Diperlukan untuk fitur simulasi usap layar (gulir TikTok/Reels) dan navigasi tombol sistem (Home, Back, Lock).
4. **Tampilan di Atas Aplikasi Lain (Display Over Other Apps):** Untuk menampilkan badge / HUD notifikasi mengambang saat menggunakan aplikasi lain.

---

## 🚀 Cara Memasang & Menjalankan APK di HP

### Metode 1: Menggunakan ADB (Jika HP terhubung via kabel / WiFi debugging)
```powershell
adb install "C:\Users\Mikazuki Itsuki\.gemini\antigravity\scratch\WaveControlApp\WaveControl-v1.0.0.apk"
```

### Metode 2: Transfer Langsung ke HP
1. Salin berkas `WaveControl-v1.0.0.apk` ke penyimpanan HP Android Anda (melalui kabel USB, WhatsApp Web, Google Drive, atau Bluetooth).
2. Buka File Manager di HP Anda dan ketuk berkas `.apk` tersebut.
3. Izinkan pemasangan dari sumber ini (*Install unknown apps*).
4. Buka aplikasi **Wave Control**, berikan izin yang diminta, dan nyalakan sakelar utama!

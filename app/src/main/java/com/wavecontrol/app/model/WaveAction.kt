package com.wavecontrol.app.model

enum class WaveAction(
    val title: String,
    val description: String,
    val category: String,
    val iconName: String
) {
    NONE(
        title = "Tidak Ada Tindakan",
        description = "Abaikan gerakan ini",
        category = "Umum",
        iconName = "Block"
    ),
    MEDIA_PLAY_PAUSE(
        title = "Putar / Jeda Media",
        description = "Kontrol musik/video di Spotify, YouTube, dll.",
        category = "Media",
        iconName = "PlayArrow"
    ),
    MEDIA_NEXT(
        title = "Lagu / Video Berikutnya",
        description = "Pindah ke trek selanjutnya",
        category = "Media",
        iconName = "SkipNext"
    ),
    MEDIA_PREV(
        title = "Lagu / Video Sebelumnya",
        description = "Kembali ke trek sebelumnya",
        category = "Media",
        iconName = "SkipPrevious"
    ),
    VOLUME_UP(
        title = "Naikkan Volume",
        description = "Menaikkan volume media utama",
        category = "Suara",
        iconName = "VolumeUp"
    ),
    VOLUME_DOWN(
        title = "Turunkan Volume",
        description = "Menurunkan volume media utama",
        category = "Suara",
        iconName = "VolumeDown"
    ),
    SCROLL_DOWN(
        title = "Gulir ke Bawah (Next TikTok/Reels)",
        description = "Usap layar otomatis ke bawah untuk konten video pendek/web",
        category = "Navigasi Layar",
        iconName = "KeyboardArrowDown"
    ),
    SCROLL_UP(
        title = "Gulir ke Atas (Prev Video)",
        description = "Usap layar otomatis ke atas",
        category = "Navigasi Layar",
        iconName = "KeyboardArrowUp"
    ),
    GLOBAL_BACK(
        title = "Tombol Kembali (Back)",
        description = "Simulasi tombol kembali sistem",
        category = "Sistem",
        iconName = "ArrowBack"
    ),
    GLOBAL_HOME(
        title = "Tombol Beranda (Home)",
        description = "Kembali ke layar utama perangkat",
        category = "Sistem",
        iconName = "Home"
    ),
    GLOBAL_RECENTS(
        title = "Aplikasi Terkini (Recents)",
        description = "Buka daftar aplikasi yang sedang dibuka",
        category = "Sistem",
        iconName = "ViewCarousel"
    ),
    GLOBAL_NOTIFICATIONS(
        title = "Buka Notifikasi",
        description = "Tarik jendela panel notifikasi",
        category = "Sistem",
        iconName = "Notifications"
    ),
    LOCK_SCREEN(
        title = "Kunci Layar",
        description = "Matikan dan kunci layar HP tanpa menyentuh",
        category = "Sistem",
        iconName = "Lock"
    ),
    TORCH_TOGGLE(
        title = "Nyalakan / Matikan Senter",
        description = "Gunakan senter HP hands-free",
        category = "Alat",
        iconName = "FlashlightOn"
    );

    val requiresAccessibility: Boolean
        get() = this in listOf(
            SCROLL_DOWN,
            SCROLL_UP,
            GLOBAL_BACK,
            GLOBAL_HOME,
            GLOBAL_RECENTS,
            GLOBAL_NOTIFICATIONS,
            LOCK_SCREEN
        )
}

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
    VOLUME_MUTE(
        title = "Mute / Hening Suara",
        description = "Senyapkan suara media secara instan",
        category = "Suara",
        iconName = "VolumeOff"
    ),
    TORCH_TOGGLE(
        title = "Nyalakan / Matikan Senter",
        description = "Gunakan senter HP hands-free",
        category = "Alat",
        iconName = "FlashlightOn"
    ),
    TORCH_ON(
        title = "Nyalakan Senter",
        description = "Hidupkan flash kamera belakang",
        category = "Alat",
        iconName = "FlashlightOn"
    ),
    TORCH_OFF(
        title = "Matikan Senter",
        description = "Padamkan flash kamera belakang",
        category = "Alat",
        iconName = "FlashlightOff"
    );

    val requiresAccessibility: Boolean
        get() = false
}

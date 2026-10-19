package com.wavecontrol.app.model

enum class GestureType(
    val title: String,
    val description: String,
    val emoji: String
) {
    NONE("Tidak Ada", "Belum ada gerakan terdeteksi", "⏳"),
    WAVE_LEFT("Lambaian ke Kiri", "Gerakan tangan melintas ke kiri", "👈"),
    WAVE_RIGHT("Lambaian ke Kanan", "Gerakan tangan melintas ke kanan", "👉"),
    WAVE_UP("Lambaian ke Atas", "Gerakan tangan mengarah ke atas", "👆"),
    WAVE_DOWN("Lambaian ke Bawah", "Gerakan tangan mengarah ke bawah", "👇"),
    HAND_HOVER("Tangan Diam / Hover", "Menahan telapak tangan di depan kamera", "✋"),
    PROXIMITY_WAVE("Sensor Jarak (Wave)", "Lambaian cepat dekat sensor layar", "📱")
}

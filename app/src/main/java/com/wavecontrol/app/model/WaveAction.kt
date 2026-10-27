package com.wavecontrol.app.model

enum class WaveAction(
    val title: String,
    val description: String,
    val category: String,
    val iconName: String
) {
    NONE(
        title = "No Action",
        description = "Ignore this gesture",
        category = "General",
        iconName = "Block"
    ),
    MEDIA_PLAY_PAUSE(
        title = "Play / Pause Media",
        description = "Control music/video in Spotify, YouTube, etc.",
        category = "Media",
        iconName = "PlayArrow"
    ),
    MEDIA_NEXT(
        title = "Next Track / Video",
        description = "Skip to the next track",
        category = "Media",
        iconName = "SkipNext"
    ),
    MEDIA_PREV(
        title = "Previous Track / Video",
        description = "Return to previous track",
        category = "Media",
        iconName = "SkipPrevious"
    ),
    VOLUME_UP(
        title = "Volume Up",
        description = "Increase primary media volume",
        category = "Sound",
        iconName = "VolumeUp"
    ),
    VOLUME_DOWN(
        title = "Volume Down",
        description = "Decrease primary media volume",
        category = "Sound",
        iconName = "VolumeDown"
    ),
    VOLUME_MUTE(
        title = "Mute / Unmute Audio",
        description = "Toggle media mute state instantly",
        category = "Sound",
        iconName = "VolumeOff"
    ),
    TORCH_TOGGLE(
        title = "Toggle Flashlight",
        description = "Turn device flashlight on or off hands-free",
        category = "Tools",
        iconName = "FlashlightOn"
    ),
    TORCH_ON(
        title = "Turn Flashlight On",
        description = "Enable rear camera flashlight",
        category = "Tools",
        iconName = "FlashlightOn"
    ),
    TORCH_OFF(
        title = "Turn Flashlight Off",
        description = "Disable rear camera flashlight",
        category = "Tools",
        iconName = "FlashlightOff"
    );

    val requiresAccessibility: Boolean
        get() = false
}

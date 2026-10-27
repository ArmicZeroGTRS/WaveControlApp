## Wave Control v1.4 — Hand-Only Recognition, Per-Node Toggles & Video Gesture Fixes

### 🚀 What's New in Version 1.4 (Build 5)

#### 1. 🖐️ Hand-Only Spatial Filtering & False-Trigger Elimination
- **Morphological Hand Bounding Box Envelope**: Added strict cluster validation (`10..185` active cells) to reject non-hand noise, wires, cables, dust, or hair.
- **2D Spatial Span Verification**: Requires motion blobs to occupy at least 3x3 grid spans in both horizontal and vertical directions, rejecting 1D linear items.
- **Energy Density & Contrast Floor**: Motion candidates must exceed an average differential contrast of 11.0f.
- **Whole-Frame Translation Rejection**: Prevents device movement, tilting, or whole-room lighting shifts from falsely triggering swipe gestures.
- **Multi-Frame Monotonic Trajectory**: Gestures must be sustained across at least 3 consecutive frames with consistent trajectory progress.

#### 2. 🎛️ Configured Gesture Nodes On/Off Toggles
- Added independent interactive toggle switches for all 4 configured gesture nodes:
  - **Wave Left** (`MEDIA_PREV` / customizable)
  - **Wave Right** (`MEDIA_NEXT` / customizable)
  - **Wave Up** (`VOLUME_UP` / customizable)
  - **Wave Down** (`VOLUME_DOWN` / customizable)
  - **Hand Hover / Hold** (`MEDIA_PLAY_PAUSE` / customizable)
  - **Proximity Wave** (`MEDIA_PLAY_PAUSE` / customizable)
- Disabling any gesture node immediately halts action dispatch (`WaveAction.NONE`) and updates the node card to an offline dimmed state with live telemetry counters.

#### 3. 🎬 Video Next Track & Previous Track Fix
- Resolved media key event dropping in video players (YouTube, Netflix, VLC, MX Player, TikTok, Reels, browser players).
- Implemented monotonic `SystemClock.uptimeMillis()` timestamps with correct `downTime` and `eventTime` offsets for `ACTION_DOWN` and `ACTION_UP`.
- Added dual dispatch: primary delivery through `AudioManager.dispatchMediaKeyEvent` plus foreground broadcast of `Intent.ACTION_MEDIA_BUTTON` (`FLAG_RECEIVER_FOREGROUND`).

#### 4. 🌙 Strict Sleep, Screen-Off & Lockscreen Guard
- Integrated `KeyguardManager.isKeyguardLocked` and `PowerManager.isInteractive`.
- Registered broadcast receivers for `ACTION_SCREEN_OFF`, `ACTION_SCREEN_ON`, `ACTION_USER_PRESENT`, `ACTION_DREAMING_STARTED`, and `ACTION_DREAMING_STOPPED`.
- CameraX use-cases and proximity listeners are fully unbound and paused when the screen is turned off, in sleep/ambient mode, or locked behind keyguard, completely eliminating unwanted battery drain and screen interference.
- Automatic resume only occurs when the user explicitly unlocks the device.

#### 5. 🛡️ 100% Mobile Banking Safe & Sandboxed Architecture
- Zero `AccessibilityService` used (eliminates banking security warnings).
- Zero Android 15 Restricted Settings needed.
- No pictures or videos are ever saved, stored, or transmitted; frames are analyzed in volatile memory and discarded immediately.

#### 6. 🏷️ Brand & Copyright
- Added official copyright notice `©2026 ARMIC ZeroGTRS` in the Settings screen under About this App.
- Updated technical specifications and build version to `Version 1.4 (Build 5)`.

---

### 📦 Package Information
- **APK File:** `WaveControl-v1.4.apk`
- **Package Name:** `com.wavecontrol.app`
- **Version Code:** `5`
- **Version Name:** `1.4`
- **Target SDK:** 35 (Android 15 Vanilla Ice Cream)
- **Minimum SDK:** 31 (Android 12 Snow Cone)
- **Compile SDK:** 36

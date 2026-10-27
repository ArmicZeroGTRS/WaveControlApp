---
name: Kinetic Telemetry Spatial UI
colors:
  surface: '#0b1326'
  surface-dim: '#0b1326'
  surface-bright: '#31394d'
  surface-container-lowest: '#060e20'
  surface-container-low: '#131b2e'
  surface-container: '#171f33'
  surface-container-high: '#222a3d'
  surface-container-highest: '#2d3449'
  on-surface: '#dae2fd'
  on-surface-variant: '#b9cacb'
  inverse-surface: '#dae2fd'
  inverse-on-surface: '#283044'
  outline: '#849495'
  outline-variant: '#3b494b'
  surface-tint: '#00dbe9'
  primary: '#dbfcff'
  on-primary: '#00363a'
  primary-container: '#00f0ff'
  on-primary-container: '#006970'
  inverse-primary: '#006970'
  secondary: '#6bd8cb'
  on-secondary: '#003732'
  secondary-container: '#29a195'
  on-secondary-container: '#00302b'
  tertiary: '#fbf3ff'
  on-tertiary: '#3f008e'
  tertiary-container: '#e3d2ff'
  on-tertiary-container: '#742fe5'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#7df4ff'
  primary-fixed-dim: '#00dbe9'
  on-primary-fixed: '#002022'
  on-primary-fixed-variant: '#004f54'
  secondary-fixed: '#89f5e7'
  secondary-fixed-dim: '#6bd8cb'
  on-secondary-fixed: '#00201d'
  on-secondary-fixed-variant: '#005049'
  tertiary-fixed: '#eaddff'
  tertiary-fixed-dim: '#d2bbff'
  on-tertiary-fixed: '#25005a'
  on-tertiary-fixed-variant: '#5a00c6'
  background: '#0b1326'
  on-background: '#dae2fd'
  surface-variant: '#2d3449'
typography:
  display-lg:
    fontFamily: Space Grotesk
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 38px
  display-lg-mobile:
    fontFamily: Space Grotesk
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 32px
  headline-lg:
    fontFamily: Space Grotesk
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '500'
    lineHeight: 22px
  body-lg:
    fontFamily: Inter
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: JetBrains Mono
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
  label-sm:
    fontFamily: JetBrains Mono
    fontSize: 10px
    fontWeight: '500'
    lineHeight: 14px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-tablet: 1.5rem
  margin: 1rem
  margin-tablet: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 0.75rem
  space-lg: 1rem
  space-xl: 1.5rem
---

## Brand & Style
This design system defines an aerospace-grade, spatial computing aesthetic tailored for real-time touchless motion detection on modern Android hardware. The interface bridges the gap between tactical avionics cockpits and consumer Material 3 refinement. Built to feel immediate, zero-latency, and technically verified, the atmosphere replaces soft consumer styling with high-fidelity telemetry, translucent obsidian glass, luminous signal conduits, and precision instrumentation.

### Visual Aesthetic & Mood
- **Tactical Spatial Minimalist:** Surface elements resemble high-index optical HUD glass, floating above an infinite midnight vacuum.
- **Instrument-Calibrated Precision:** Strict reliance on hairline borders, monospaced coordinate telemetry, and luminous signal vectors rather than diffuse drop shadows.
- **Zero-Friction State Feedback:** Clear transitions between dormant obsidian readiness and vibrant cyan/teal kinetic capture.
- **Hardware Integration Aura:** Designed to feel native to high-refresh-rate OLED panels, utilizing pure dark canvases to minimize display power draw while ensuring sensor vectors radiate with optical clarity.

## Colors
The palette leverages a deep obsidian substrate to achieve high dynamic contrast against glowing kinetic telemetry conduits. The primary colors establish technical hierarchy, signal priority, and interactive states across the application.

### Palette Architecture
- **Primary (`#00F0FF` - Luminous Cyan):** The central kinetic beacon. Used for active motion vectors, live tracking reticles, high-priority interactive controls, and positive gesture confirmation pulses.
- **Secondary (`#0D9488` - Cyber Teal):** Calibrated operational state. Applied to stabilized sensor controls, proximity thresholds, verified background services, and balanced hardware settings.
- **Tertiary (`#7C3AED` - Electric Violet):** Reserved for media/entertainment action mapping, deep-system calibration states, and advanced machine-learning sensitivity thresholds.
- **Neutral (`#0F172A` - Deep Slate Navy):** The structural surface container tone. Provides depth above the root canvas (`#0B0F19`) while establishing boundaries for glassmorphic telemetry cards.

### Extended Semantic Roles
- **Operational Emerald (`#10B981`):** Active daemon heartbeat, verified security indicators, and foreground gesture capture readiness.
- **Impulse Amber (`#F59E0B`):** Sensor threshold limits, real-time kinetic spikes, and active ambient calibration notices.
- **Terminal Red (`#EF4444`):** Sensor occlusion warnings, missing optical permissions, and hardware cutoff indicators.
- **Subdued Hairline Outline (`#334155`):** Structural borders providing 1px delineation between telemetry tiers without emitting ambient noise.

## Typography
The typographic framework utilizes a dual personality: technical geometric impact for high-level spatial states via Space Grotesk, neutral structural readability for configurations via Inter, and tabular, non-shifting sensor metrics via JetBrains Mono.

### Typographic Mechanics
- **Geometric Headers:** Space Grotesk creates an authoritative avionics feel, maintaining slight letter-spacing reductions (`-0.01em`) on large headlines to guarantee visual cohesion in high-density viewport headers.
- **Neutral Workhorse Body:** Inter delivers clean edge definition against deep dark-mode backgrounds, engineered with open counters to eliminate reading fatigue during extended operational monitoring.
- **Tabular Telemetry Monospace:** JetBrains Mono is required for all real-time coordinate variables (`X: +0.42`, `Y: -0.15`), frame counters, and sensitivity percentages. The fixed-width character bounding boxes prevent coordinate jitter during 60 FPS real-time vector recalculations.

## Layout & Spacing
The layout follows a strict 4px/8px modular rhythm, prioritizing touch-target compliance, ergonomic thumb reach zones on handheld devices, and structured horizontal density for spatial telemetry.

### Viewport Adaptation & Structure
- **Mobile Handheld (Phone):** Fluid 4-column container bounded by `1rem` outer canvas margins and `1rem` column gutters. Real-time interactive viewports (e.g., optical tracking radar cards) adhere to a 16:9 aspect ratio to maintain dimensional fidelity with front-facing camera aspect ratios.
- **Tablet / Large Foldable:** Expanded 8-column to 12-column layout utilizing `2rem` margins and `1.5rem` gutters. Splits telemetry scopes to the left panel while gesture-to-action bindings remain fixed in a persistent vertical list to the right.
- **Component Insets:** Internal card paddings use `space-lg` (`1rem`) to `space-xl` (`1.5rem`), ensuring tactile controls never crowd data visualizations.
- **Ergonomic Safety:** Touch targets consistently respect the minimum 48px by 48px envelope, offset with explicit bottom safe-area insets to clear Android 3-button and gesture pill navigation layers.

## Elevation & Depth
Elevation is constructed through physical light transmission, edge luminosity, and tonal stratification rather than traditional drop shadows. In this system, depth communicates the operational priority and responsiveness of the sensor stack.

### Layer Architecture
- **Layer 0 (Infinite Vacuum - `#0B0F19`):** Root canvas scaffold. Fully opaque, high-efficiency OLED baseline.
- **Layer 1 (Sub-Surface Telemetry - `#0F172A`):** Telemetry viewports and inert control groupings. Bound by a crisp 1px outline in `#334155` at 40% opacity.
- **Layer 2 (Interactive Floating Glass - `#1E293B` at 70% opacity):** Active gesture mapping rows, setting cards, and floating calibration bars. Features a real-time `backdrop-blur(16px)` treatment and a 1px boundary stroke of `#38BDF8` at 25% opacity.
- **Layer 3 (Ephemeral HUD Flight Layer - `#CC0F172A`):** Ephemeral system overlay pills and heads-up feedback banners. Elevated above all content with a subtle ambient neon halo: `box-shadow: 0 0 20px rgba(0, 240, 255, 0.15)`.

## Shapes
Shapes express precision engineering and technical containment. Radii maintain tight, controlled proportions that keep corners distinct without appearing aggressive or brutalist.

### Shape Scale Rules
- **Micro Radii (`rounded-sm` / 4px - 6px):** Directional telemetry indicators, vector reticle crosshairs, and live frame-rate indicators.
- **Pill Radii (`rounded-full` / 9999px):** Status badges, floating system HUD confirmation capsules, and primary command buttons.
- **Control Containers (`rounded-md` / 8px - 12px):** Action mapping dropdown tiles, gesture calibration tracks, and input fields.
- **Structural Modules (`rounded-lg` to `rounded-xl` / 16px - 24px):** Radar viewports, telemetry cards, and master power hero modules.

## Components

### Buttons
- **Primary Interactive Button:** Full pill shape (`rounded-full`), solid `#00F0FF` fill with high-contrast `#05131D` typography. Focus states trigger a 2px offset border in `#38BDF8`.
- **Secondary Telemetry Outlined Button:** Transparent background, 34px fixed height, 1px solid `#334155` border, transitioning to `#00F0FF` on pointer or spatial proximity. Text color `#F8FAFC`.
- **Tactile Toggle Switch:** 48px touch envelope with an oversized pill track. Dormant track in `#1E293B` with `#64748B` thumb; Active track shifts to `#10B981` with a luminous `#FFFFFF` core emitting a soft `#10B981` radial bloom.

### Status HUD Capsules & Chips
- **Sensor Readiness Badges:** Compact pill chips (`rounded-full`, 24px height) featuring a 6px circular dot indicator. State green (`#10B981`) for camera lock, state amber (`#F59E0B`) during sensor calibration, and state red (`#EF4444`) for permission failure. Accompanying label uses `label-sm` in JetBrains Mono.
- **Floating Ephemeral HUD Overlay:** Centered top-of-screen floating capsule (44px height, `rounded-full`). Background `#CC0F172A` with `backdrop-filter: blur(12px)` and a 1px border of `#00F0FF` at 50% opacity. Contains a vector gesture glyph on the left and bold action confirmation text on the right. Auto-dismisses with an exponential ease-out curve.

### Telemetry Cards & Viewports
- **Optical Motion Radar Viewport:** 16:9 ratio, `#0F172A` background, 1px border in `#38BDF8` at 30% alpha. Contains an animated 64px central circular reticle flanked by four directional directional badges (`#1E293B` dormant, turning `#00F0FF` upon directional threshold breach). Top header features real-time monospaced vector telemetry.
- **Gesture Action Tile:** Surface container (`#131D31`) with a 44px circular icon hub, primary action title in `title-md`, and an integrated hairline dropdown selector for action assignment.

### Sliders & Calibration Inputs
- **Stepped Sensitivity Slider:** Continuous 4px height track in `#1E293B` with filled active track in `#00F0FF`. Thumb is a 18px circle with a solid `#FFFFFF` center and a 2px `#00F0FF` ring. Real-time numerical feedback sits directly above the thumb, rendered in JetBrains Mono.
- **Interactive Checkboxes & Radio Controls:** Square (4px radius) or circular elements bordered in `#334155`. Upon selection, fills with `#00F0FF` and displays a high-contrast `#05131D` vector mark.
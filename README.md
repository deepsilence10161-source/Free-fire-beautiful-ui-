# 🌸 Sakura Strike

Cute anime-style battle-royale mini game — **hamara apna code, apni kala** (koi bhi Garena/Free Fire ka asset, code ya APK use nahi kiya gaya).

## Kya hai isme (v0.1)
- 🌸 **Title screen** — sakura petals animation, gradient logo
- 🎀 **Lobby** — avatar card, level/XP bar, coins, mission, mode strip
- 👗 **Shop** — 6 cute skins (khareedo / pehno) · 🏅 **Ranks** — stats · ⚙️ **Settings**
- 🎮 **Match** — top-down cute shooter: joystick + fire button, auto-aim (accuracy), 8 bots, safe zone, HP, kills, timer
- 🏆 **Result** — coins + XP + level up
- 📱 **Android APK** — WebView shell, fullscreen, back = pause

## Mobile par khelne ke 2 tareeke
1. **Turant:** `game/index.html` kholo (browser me chalega, touch controls ready).
2. **APK:** GitHub Actions → **Build Sakura Strike APK** → run → artifact **SakuraStrike-apk** download → install.

## Repo layout
```
game/index.html                  poori game (HTML+CSS+JS, single file, offline)
android/                         Android WebView shell (Java, koi library nahi)
  app/src/main/assets -> ../game (gradle sourceSets se)
.github/workflows/build-apk.yml  APK build + upload
assets/icon-512.png              app icon
```

## Controls
| Kaam | Mobile | Desktop |
|---|---|---|
| Chalna | left joystick | WASD / arrow |
| Goli | 🔫 button | Space / mouse click |
| Aim | auto-aim (Settings me off kar sakte ho) | mouse |

## Agla plan (v0.2)
- [ ] 3D-style lobby animations, character rotation
- [ ] Bots ki smart AI + squads
- [ ] Daily missions, rank tiers, spin wheel
- [ ] Sound/music (royalty-free), haptics
- [ ] Emulator-based auto UI test (GitHub Actions) + screenshots

*Made with 💗 for a mobile-only player.*

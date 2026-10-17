# Third-Party Notices — PHONE SURVIVAL 1.2.0

The APK build bundles these third-party open-source game collections. Upstream source is fetched at the exact commits listed below by the GitHub Actions workflow. Their interfaces and game code remain under their upstream licenses; the PHONE SURVIVAL app does not relicense that third-party code.

## 1. Zeli Web Games — 150 mini-games

- Source: https://github.com/hajunho/zeli-web-games
- Pinned source commit: ba9938eb222cb588ae7b0a6056ba6bdda89dcd9c
- License: Apache License 2.0
- Included portion: the built static web-game collection generated from the upstream source using its package lock and Vite build.
- The complete upstream license is included in the APK at assets/opensource/licenses/Zeli-Apache-2.0-LICENSE.txt.

## 2. Mini Browser Games — 115 active single-file HTML5 games

- Source: https://github.com/wangzifan396-wzf/mini-browser-games
- Pinned source commit: e459369b31c35658855e7ef50b46fa9f994750d6
- License: MIT License
- Included portion: the 115 filenames listed in GAME_TIERS.json under SSS, SS, S, and A tiers. The nine E-tier archived games and the separate desktop/multiplayer products are not bundled.
- The complete upstream license is included in the APK at assets/opensource/licenses/Mini-Browser-Games-MIT-LICENSE.txt.

The game collection is built into the APK so the local library can be opened without downloading games at runtime. Individual game interfaces may retain their upstream languages and design.

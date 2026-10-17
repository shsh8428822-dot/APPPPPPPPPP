# PHONE SURVIVAL — v1.2.0

An offline-first Android toolbox built with Kotlin and Jetpack Compose.

## Included features

- **12 persistent color themes** and a compact-layout setting that changes spacing throughout the app.
- Local notes and preferences; the Android manifest does not request general internet access.
- **Password-protected private memory ("black memory")** stored on-device and encrypted with AES-GCM using a PBKDF2-derived key. It supports multiple encrypted notes, creating and deleting notes, changing the password, and a temporary lockout after five failed password attempts.
- Tools that work locally: calculator, stopwatch, countdown timer, unit converter, cryptographically strong random password generation, random-number picker, persistent emergency checklist, battery status, flashlight (camera permission requested only when opened), and compass (when compatible hardware exists).
- **19 built-in offline mini-games** in the native Compose game list.
- **265 additional open-source browser games bundled into the APK by GitHub Actions**:
  - 150 mini-games from [hajunho/zeli-web-games](https://github.com/hajunho/zeli-web-games) (Apache License 2.0).
  - 115 active single-file HTML5 games selected from [wangzifan396-wzf/mini-browser-games](https://github.com/wangzifan396-wzf/mini-browser-games) (MIT License).
- The open-source library has a local search, collection switch, and offline WebView player. It only serves packaged app assets and blocks requests to external hosts.

## Build and download

GitHub Actions downloads the two pinned open-source game sources, builds the Zeli web app, bundles the 115 active standalone games, and then compiles the Android APK. **The games are bundled into the resulting APK; after installation they do not need a connection to fetch game files.**

Open the [GitHub Actions page](https://github.com/shsh8428822-dot/APPPPPPPPPP/actions), choose the latest successful **Android APK** run, and download the **phone-survival-debug-apk** artifact. The artifact is a ZIP containing the APK file.

## Third-party sources and licenses

- [Zeli Web Games](https://github.com/hajunho/zeli-web-games), Apache-2.0, pinned to commit ba9938eb222cb588ae7b0a6056ba6bdda89dcd9c.
- [Mini Browser Games](https://github.com/wangzifan396-wzf/mini-browser-games), MIT, pinned to commit e459369b31c35658855e7ef50b46fa9f994750d6. The bundle includes only the 115 active HTML games listed in its SSS, SS, S, and A tiers; the 9 archived games and separate desktop/multiplayer products are excluded.
- Full license files are packaged in the APK under assets/opensource/licenses/ and documented in [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

Some third-party mini-games retain their original interface languages (mainly Korean/English), and some standalone games have Chinese-language UI. The PHONE SURVIVAL app and the library launcher are in Hebrew.

## Device notes

- Minimum Android version: Android 8.0 (API 26).
- The flashlight requests camera permission only when the flashlight tool is opened.
- The compass needs a compatible device sensor.
- The browser game player serves bundled local assets and blocks requests to other hosts. The app manifest contains no INTERNET permission.

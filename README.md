# PHONE SURVIVAL — v1.1.0

An offline-first Android toolbox built with Kotlin and Jetpack Compose.

## Features in this build

- **12 persistent color themes** and a compact-layout setting that actually changes spacing.
- Local notes and preferences; the app does not request internet access.
- **Password-protected private memory** encrypted on-device with AES-GCM and a PBKDF2-derived key. The private memory includes a temporary lockout after five failed password attempts and an in-app password-change flow.
- Offline tools: calculator, stopwatch, countdown timer, unit conversion, random-number picker, password generator, persistent emergency checklist, battery status, flashlight (camera permission requested only when opened), and compass (if the device has a compatible sensor).
- A searchable library of built-in offline mini-games: number guessing, tic-tac-toe, arithmetic challenge, memory pairs, sliding-number puzzle, word scramble, reaction test, dice, coin flip, number sequence, rock-paper-scissors, general-knowledge quiz, tap challenge, Simon-style color sequence, higher/lower cards, 2048, minesweeper, Hebrew hangman, and number memory.

## Build the APK

GitHub Actions builds a debug APK on each push to `main`. Open the [Actions page](https://github.com/shsh8428822-dot/APPPPPPPPPP/actions) and download the `phone-survival-debug-apk` artifact from a successful run.

## Game-library scope

The current build contains the original mini-games listed above, included in this repository's source. **The requested 250 third-party open-source games are not yet bundled.** Adding that collection requires selecting each project, reviewing its license, adapting it for Android/offline use, and testing it; this repository does not claim that work is complete.

## Device notes

- Minimum Android version: Android 8.0 (API 26).
- The flashlight requests the camera permission only when the flashlight tool is opened.
- The compass depends on device sensor support.
- No network permission is declared in the app manifest.

# PHONE SURVIVAL

An offline-first Android utility toolbox with a dark green visual style.

## Current foundation
- Kotlin + Jetpack Compose UI
- Theme customization (green, blue, purple, gold and orange)
- Local settings and notes
- Password-protected encrypted private vault
- Built-in utility screens and starter mini-games
- No network permission in the app manifest
- GitHub Actions builds a debug APK on each push to `main`

## Build
The GitHub Actions workflow runs `gradle assembleDebug` and uploads the APK as an artifact named `phone-survival-debug-apk`.

## Important implementation scope
This repository starts the application foundation. The planned 250 third-party open-source games are not yet bundled: each game must be selected, license-reviewed, adapted for Android, and tested offline before inclusion. The same applies to any device-specific tool requiring hardware permissions or sensors.

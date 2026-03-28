# Lviv Architecture Explorer

Semester Android project: a map-based app to explore architecture sights in Lviv, filter them, read details, and track personal visited/want-to-visit diary entries.

## Stack

- Kotlin + Jetpack Compose
- Hilt (dependency injection)
- Room (local persistence)
- DataStore (local profile/settings)
- Coroutines + Flow
- osmdroid (OpenStreetMap)

## Features

- Map screen with markers from local data
- Category filter screen (multi-select)
- Place detail screen with quick diary actions
- Diary screen with `Visited` and `Want to visit` tabs
- Settings screen with local profile and theme mode

## Data source

- MVP uses static JSON in `app/src/main/assets/sights_lviv.json`
- First launch imports JSON into Room on background thread (`Dispatchers.IO`)
- Optional next step: add Retrofit API and keep Room as cache

## Project structure

- `app/src/main/java/com/angelina/lvivexplorer/navigation/` - app routes and nav host
- `app/src/main/java/com/angelina/lvivexplorer/feature/` - screen UI + ViewModels
- `app/src/main/java/com/angelina/lvivexplorer/domain/` - models, repository interfaces, use cases
- `app/src/main/java/com/angelina/lvivexplorer/data/` - repository implementations, Room, DataStore, JSON source
- `app/src/main/java/com/angelina/lvivexplorer/di/` - Hilt modules

## Requirement checklist

- [x] 5+ screens
- [x] Blocking I/O off UI thread
- [x] Data persistence (Room + DataStore)
- [x] Dependency injection (Hilt)
- [x] Reasonable project file organization
- [x] Extra feature: map integration
- [ ] Git repository with commit history (initialize and commit locally)

## Build/run

1. Open project in Android Studio (Ladybug+ recommended).
2. Let IDE sync Gradle dependencies.
3. Run the `app` configuration on an Android emulator/device.

The app uses OpenStreetMap tiles via `osmdroid`, so no Google API key is required.

## Tests

- `PlaceMappingTest` verifies JSON DTO -> Room mapping.
- `DiaryRepositoryImplTest` verifies diary add/update repository behavior.

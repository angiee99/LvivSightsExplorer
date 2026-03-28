# Lviv Architecture Explorer

A map-based app to explore architecture sights in Lviv, filter them, read details, and track personal visited/want-to-visit diary entries. Letterbox and Goodreads inspired, but for travel lovers.

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

## Project structure

- `app/src/main/java/com/angelina/lvivexplorer/navigation/` - app routes and nav host
- `app/src/main/java/com/angelina/lvivexplorer/feature/` - screen UI + ViewModels
- `app/src/main/java/com/angelina/lvivexplorer/domain/` - models, repository interfaces, use cases
- `app/src/main/java/com/angelina/lvivexplorer/data/` - repository implementations, Room, DataStore, JSON source
- `app/src/main/java/com/angelina/lvivexplorer/di/` - Hilt modules


## Build/run

1. Open project in Android Studio (Ladybug+ recommended).
2. Let IDE sync Gradle dependencies.
3. Run the `app` configuration on an Android emulator/device.
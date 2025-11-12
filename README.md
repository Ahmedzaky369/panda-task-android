# Panda Task - Native Android App

A native Android implementation of the Panda Task PWA, built with Jetpack Compose and Material 3.

## Project Structure
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Architecture**: MVVM
- **Database**: Room
- **Navigation**: Jetpack Navigation Component
- **Target SDK**: 34, Min SDK: 26
- **Package**: com.pandatask.app

## Features
- ✅ Today's tasks with drag-and-drop reordering
- ✅ All tasks organized by categories  
- ✅ Weekly and monthly goals tracking
- ✅ Dark theme with green accent (#50C878)
- ✅ Offline-first with Room database
- ✅ Sound effects and animations
- ✅ Material 3 design system

## Build Instructions

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Run the app on device/emulator

## Architecture Overview

```
app/
├── data/
│   ├── database/        # Room database, DAOs, entities
│   ├── repository/      # Repository pattern for data access
│   └── preferences/     # SharedPreferences wrapper
├── domain/
│   ├── model/           # Domain models
│   └── usecase/         # Use cases for business logic
├── presentation/
│   ├── ui/
│   │   ├── screens/     # Screen composables
│   │   ├── components/  # Reusable UI components
│   │   └── theme/       # Material 3 theme
│   └── viewmodel/       # ViewModels
└── MainActivity.kt      # Single activity with Compose
```
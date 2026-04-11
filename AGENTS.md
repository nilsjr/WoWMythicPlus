# Project Analysis: WoWMythicPlus

This project is a website for World of Warcraft players to inspect their characters' Mythic Plus runs, built using Kotlin Multiplatform and Compose HTML.

## Architecture

- **Multiplatform Architecture**: Separation of concerns between UI (`:web`) and Data (`:character-data`).
- **Compose HTML Frontend**: UI is built using `compose.html`, providing a declarative way to define DOM and CSS.
- **Repository Pattern**: Data access is abstracted via `RaiderIoRepository`, with `CharacterUsecase` serving as the bridge to the UI.
- **Dependency Injection / State management**: Uses `CompositionLocalProvider` in the UI to provide instances like `CharacterUsecase` down the component tree.
- **State Persistence**: Uses `browser.localStorage` to persist user preferences like dark mode.
- **Root Rendering**: Entry point `Main.kt` binds the Compose runtime to `<div id="root">`.

## Module Structure

- **`:web`**: The JS/Browser frontend module. Contains UI components, pages, and styling.
- **`:character-data`**: Kotlin Multiplatform module (Common, JS, Android) containing:
    - **APIs**: Ktor-based implementation for Raider.io.
    - **Models/Entities**: Data classes for API responses and domain logic.
    - **Repositories/Usecases**: Logic for fetching and processing character data.
- **`:project-setup`**: Build logic module containing Gradle Convention Plugins (Detekt, Kotlin) to ensure consistent configuration across modules.

## Technologies & Patterns

- **Kotlin Multiplatform**: Shared logic between platforms.
- **Compose HTML**: Specifically `compose.html.core` and `compose.html.svg`.
- **Ktor Client**: For asynchronous network requests to Raider.io.
- **Kotlinx Serialization**: For JSON parsing.
- **Kotlinx Coroutines**: For handling asynchronous tasks.
- **Detekt**: Static code analysis with custom convention plugins.
- **Type-safe Project Accessors**: Enabled for better Gradle dependency management.

## Key Gradle Tasks

- `jsBrowserProductionWebpack`: Compiles and bundles the application for production.
- `moveAssets`: Copies assets to the distribution directory.
- `moveExecutable`: Assembles the final `index.html`, JS bundles, and assets into `build/dist/`.
- `ktlintCheck`: Runs detekt with formatting/linting rules.
- `./gradlew jsBrowserDevelopmentRun`: Serves the application at http://localhost:8080.

## Project Layout

- `web/src/jsMain/kotlin`: Main UI application logic.
- `character-data/src/commonMain/kotlin`: Shared data logic and network clients.
- `project-setup/src/main/kotlin`: Custom Gradle plugins.
- `src/jsMain/resources`: Static assets like `index.html` and images.

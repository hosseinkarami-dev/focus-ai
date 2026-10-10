# FocusAI

FocusAI is a Kotlin Multiplatform / Compose Multiplatform productivity app that turns notes into user-reviewed task suggestions. Its current shared app supports local task and note capture, Inbox search, task completion, and a clearly labeled sample suggestion flow.

## Product and engineering documentation

- [Product specification](docs/product/product-specification.md)
- [Design system](docs/design/design-system.md)
- [Screen specifications and navigation](docs/design/screen-specifications.md)
- [Product and design decisions](docs/design/decisions.md)
- [Architecture](docs/architecture/architecture.md)
- [AI contracts](docs/architecture/ai-contracts.md)
- [Testing strategy](docs/testing/testing-strategy.md)
- [Implementation roadmap](docs/roadmap/implementation-roadmap.md)

## Current project state

The shared module targets Android, iOS arm64, and iOS simulator arm64. It uses Compose Material 3, shared MVVM state, Koin, Room 3 with bundled SQLite, Ktor with kotlinx.serialization JSON, and Material icon vectors. The FocusAI focus mark is shared by Compose and Android/iOS launchers. An indigo, lilac, and apricot palette supports Light, Dark, and System modes. Tasks, notes, settings, and focus sessions are stored locally; Room schema v8 adds a saved language choice through automatic migrations. Tasks can be created, edited, searched, filtered, completed with Undo, manually reordered, and deleted with confirmation. Inbox notes can be searched, filtered by review/archive status, reopened, edited, archived/restored, and deleted with confirmation. Settings persist appearance, English/Persian/Spanish language, and a 15/25/45/60-minute focus default. Persian enables right-to-left layout, and dates follow the selected language. Focus sessions can be linked to an open task, paused, resumed, finished, or discarded; recent session history survives relaunch. Completion feedback uses a branded toast with Undo. Suggestions use a clearly labeled sample provider, so no note is sent over the network. Explainable activity insights and a secured AI backend remain future work.

## Running the apps

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

# FocusAI

**A calmer way to turn the things on your mind into focused, manageable work.**

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF?logo=kotlin&logoColor=white)
![Platforms](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS-5965E0)

FocusAI is a privacy-minded productivity app for Android and iOS. Capture a thought in your Inbox, shape it into a task when you are ready, and make room for focused work with a built-in timer. Tasks, notes, preferences, and focus history are stored locally on your device.

> **AI status:** task suggestions currently use a clearly labeled sample provider. No AI service is contacted and your notes are not uploaded.

## What you can do

- **Capture without losing momentum:** save and edit notes in a searchable Inbox; review, archive, restore, or delete them.
- **Plan your work:** create and edit tasks with due dates, reminders, priorities, time estimates, repeat rules, and checklists.
- **See your workload your way:** search and filter tasks, sort by your order, due date, priority, or title, and switch between a task list and a date-based week view.
- **Keep plans close:** use home-screen widgets and launcher shortcuts to view tasks, add a task, or capture a thought.
- **Focus in sessions:** link a task to a timer, pause or resume a session, review your history and insights, and set a daily focus goal. Optional break reminders are available.
- **Make it yours:** choose Light, Dark, or System appearance and use the app in English, Persian, Spanish, French, or Arabic. Persian and Arabic use right-to-left layout.
- **Stay in control:** task suggestions remain a preview until you choose what to keep. Local task and note workflows do not require an account or network connection.

## Screens and entry points

| Today | Inbox | Tasks | Focus | Settings |
| --- | --- | --- | --- | --- |
| Daily priorities and a clear starting point | Searchable notes and review flow | Search, filters, sorting, and week planning | Timer, goals, history, and insights | Appearance, language, and reminders |

Android provides a home-screen widget and launcher shortcuts. iOS provides a WidgetKit widget and launcher quick actions. Their task and capture links open the corresponding app flows.

## Built with

- Kotlin Multiplatform and Compose Multiplatform with Material 3
- MVVM presentation state shared across Android and iOS
- Koin dependency injection
- Room with bundled SQLite for local persistence
- Ktor and kotlinx.serialization for the provider/network boundary
- Native Android notifications and iOS UserNotifications for local reminders
- WidgetKit on iOS and an Android home-screen widget

The shared module targets Android, iOS arm64, and iOS simulator arm64. Platform-specific widget, notification, launcher, and app-entry code lives alongside the shared UI and data layers.

## Build

### Android

```sh
./gradlew :androidApp:assembleDebug
```

### iOS

Open [`iosApp/iosApp.xcodeproj`](iosApp/iosApp.xcodeproj) in Xcode and build the `iosApp` scheme for an iOS device or simulator. The iOS widget extension is included in the app target. App Group capabilities must be configured for the signing team to install and refresh the widget on a device.

## Project structure

```text
androidApp/   Android app entry point, notifications, launcher actions, and widget
iosApp/       SwiftUI app entry point, WidgetKit extension, and iOS project
shared/       Compose UI, MVVM state, Room data, localization, and shared platform APIs
docs/         Product, design, architecture, testing, and roadmap documentation
```

## Project documentation

- [Product specification](docs/product/product-specification.md)
- [Design system](docs/design/design-system.md)
- [Screen specifications](docs/design/screen-specifications.md)
- [Architecture](docs/architecture/architecture.md)
- [AI contracts](docs/architecture/ai-contracts.md)
- [Implementation roadmap](docs/roadmap/implementation-roadmap.md)
- [Testing strategy](docs/testing/testing-strategy.md)

## Current status

The core local-first flows are implemented, including task and note management, focus sessions, reminders, recurring tasks, daily focus goals, localization, Android and iOS home-screen entry points, and settings. Accessibility and larger-text review and on-device workflow verification remain part of the roadmap. See the [implementation roadmap](docs/roadmap/implementation-roadmap.md) for details.

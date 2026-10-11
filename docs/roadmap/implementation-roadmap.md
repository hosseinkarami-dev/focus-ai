# Implementation roadmap

The project has completed an initial compatibility and UI foundation pass: Compose Material 3 theme and five tabs, a shared/native FocusAI focus mark, an indigo/lilac light/dark palette with apricot accent, Room 3 task/note/settings/session persistence, Koin, shared MVVM state, Ktor with JSON serialization and a typed HTTPS adapter, Inbox search/status filters/edit/archive/restore/delete, local note dates, full-height task create/edit screens with calendar date selection, daily/weekly/monthly recurring tasks, local due reminders on Android and iOS, task search and status filters, completion Undo with branded feedback, persisted manual task order, task checklist steps, editable theme/language/focus preferences, timestamp-based focus controls and recent session history, daily focus goals and optional break reminders, English/Persian/Spanish/French/Arabic interface copy with RTL for Persian and Arabic, Manrope/Noto Sans Arabic typography, explainable 7/30-day focus insights with task-completion timestamps, and a sample-default note suggestion/review flow. An optional OpenAI-backed Ktor service is now included; apps still use sample suggestions until an HTTPS endpoint is configured.

## Phase 0 — Compatibility spike and app foundation

**Status:** library versions are pinned. Room/KSP generation, schema v1→v2→v3→v4→v5→v6→v7→v8→v9→v10→v11→v12 migration generation, Android debug assembly, iOS arm64 shared-source compilation, and the full iOS simulator app build succeeded in earlier compatibility work. A generic iPhoneOS app build now also succeeds. System/Light/Dark theme, English/Persian/Spanish/French/Arabic language and focus preferences are persisted and editable. Secondary task editing and destination changes animate. A saveable state holder retains each destination's scroll and saveable UI state while switching tabs. The shared Back handler closes task editing first, then returns any secondary destination to Today. Device-level verification remains.

**Acceptance:** Android and iOS shared app compile; five destinations preserve state; Back closes nested task editing and returns from secondary destinations to Today on both platforms; theme switch renders light/dark/system; no unsupported dependency remains speculative. The new navigation behavior still needs iOS compilation and platform verification.

## Phase 1 — Domain and local persistence

**Work:** domain types, repository contracts, Room schema/entities/DAOs/mappers, preferences, DI modules, fake clock, note/task/subtask/session storage.

**Acceptance:** local CRUD and Flow observation work offline; atomic selected-suggestion write API exists; migration policy and tests are in place.

## Phase 2 — Core manual productivity flows

**Work:** Today, Inbox, editor, task list/details, planner order, completion/undo and empty/error states.

**Status:** local notes and task CRUD are implemented. Task search and All/Open/Completed filters are available; task edits retain completion and creation metadata. Completion changes offer Undo, active tasks can be reordered through accessible controls saved in Room, and the full-height task editor supports calendar-selected dates and editable checklist steps. Inbox supports search, review/archive filters, local update dates, archive/restore, and confirmed permanent deletion with an Undo action that restores the complete Room entity. Screen-reader labels now cover task completion, reorder, edit, archive, restore, and delete actions in all five interface languages. Removed explicit 34/36 dp sizing from task reorder and inbox archive/delete controls so Material's 48 dp minimum interactive sizing applies. Platform-level touch-target, screen-reader-order, and larger-text checks remain.

**Acceptance:** user can capture a note and create/edit/complete/reorder/repeat tasks, schedule local due reminders, and use the core flows with no network; state is reflected across entry points; accessibility and large-text behavior reviewed on supported platforms.

**Next additions (ordered):** complete accessibility and larger-text review. Android and iOS home-screen widgets plus platform launcher shortcuts are implemented. Backup and cross-device sync are out of scope.

**Home-screen entry points:** Android launcher shortcuts open task creation or Inbox capture directly. The Android home-screen widget shows the next three open tasks and offers one-tap task and thought capture; it refreshes on app resume and periodically. iOS launcher quick actions open the same flows. The WidgetKit extension shows a shared snapshot of up to three open tasks and links to task creation, task list, and thought capture; the app publishes updates through its App Group and refreshes timelines when backgrounded.

**Focus goals and breaks:** daily focus goal choices (60–240 minutes) persist in Room schema v12 and display a local-day progress indicator on the Focus screen. Optional break reminders request notification access before enabling, fire after a focus session is finished, and sessions auto-finish at zero while the app is active. Android debug assembly and shared iOS arm64 compilation succeeded; notification delivery, time-zone boundaries, and the v11→v12 migration need device/database verification.

**Validation update:** Android debug assembly and shared iOS arm64 compilation passed. The iOS app and WidgetKit extension built for generic iPhoneOS with code signing disabled; no simulator was launched. iOS static launcher quick actions and widget deep links route into task creation, task list, and thought capture.

**Recurring tasks:** implemented daily, weekly, and monthly recurrence in Room schema v10. Recurring tasks require a due date. Completing an occurrence creates the next open occurrence and copies its checklist with unchecked steps; Undo removes that generated occurrence. Month-end dates clamp to the last valid day of the next month. Android debug assembly and KSP migration generation succeeded; migration behavior still needs an installed-database test.

**Due reminders:** tasks can store a local reminder time alongside their due date. Android schedules local notifications, restores pending reminders after reboot/app replacement, and links users to notification settings when scheduling is unavailable. iOS requests notification authorization and schedules local notifications. Completing a recurring task schedules its next occurrence at the same local time. Android debug assembly and iOS arm64 shared-source compilation succeeded; notification delivery, permission flows, and database upgrades still need device verification.

**Weekly planning:** the Tasks destination has a segmented List/Week selector and a sort menu beside search (manual order, due date, priority, or title). Week view selects a Monday–Sunday range, shows open-task counts by day, and shows only tasks scheduled for the selected date. Unscheduled tasks remain available in List view and no longer repeat under every day. Adding a task from Week view pre-fills the selected date. The selected week and day are saveable while switching destinations. Device-level layout, date-locale, large-text, and accessibility review remain.

## Phase 3 — Focus and explainable insights

**Status:** focus sessions persist start, pause, resume, finish and discard transitions in Room. Elapsed time is derived from saved timestamps, sessions optionally link to open tasks, and recent history survives app recreation. The active session is restored from Room when the app starts. The Focus screen now shows rolling 7/30-day completed focus minutes, session count, average session length, timestamped task completion count, and a seven-bucket completed-session trend. Task completion timestamps are stored from schema v9 onward; pre-v9 completed tasks have no knowable completion time and are excluded with an explanatory note. Device lifecycle verification remains.

**Work:** persisted timestamp timer transitions, optional task linkage, history, derived metrics and settings/privacy controls.

**Acceptance:** timer survives backgrounding/recreation and uses timestamps; abandon/discard behavior is clear; each metric matches documented records and period semantics. Insights use rolling windows of exactly 7×24 or 30×24 hours ending at the current time. Trend buckets divide the selected window into seven equal intervals; task completion counts use `completedAtEpochMillis`, while focus metrics include only sessions whose outcome is `COMPLETED` and whose end timestamp is in the selected window. Focus minutes and average length sum `accumulatedRunningMillis`; discarded and incomplete sessions are excluded.

## Phase 4 — AI contracts and deterministic mock

**Work:** versioned DTOs, validation/normalization, provider abstraction, mock scenarios, extraction/breakdown/planning review state, explicit confirmation.

**Acceptance:** all mock scenarios are deterministic; invalid/partial/duplicate output recovers safely; no proposal persists before selected confirmation; existing subtasks remain untouched.

## Phase 5 — Secure backend integration

**Status:** the shared Ktor adapter now selects the sample provider by default and uses a configured HTTPS endpoint when present. The separate `aiBackend/` service exposes the versioned extraction endpoint, calls OpenAI Responses API with strict structured output, keeps credentials in server environment variables, validates/normalizes proposals, limits note size, and applies a basic in-memory per-IP rate limit. The editor shows a transmission disclosure only when remote mode is configured; suggestions still require confirmation before Room writes.

**Remaining:** deploy behind HTTPS with gateway-level abuse protection, choose the production model, review data-retention terms, and exercise timeout/retry behavior against a staging deployment. The in-memory limiter is for a single backend process and is not a complete public-service abuse defense.

**Acceptance:** app contains no provider secret; backend owns credentials and model selection; offline/timeout/rate-limit/retry paths preserve input; only explicit AI action transmits note content.

## Phase 6 — Polish and release readiness

**Status:** Android release signing can be supplied through environment variables without checking secrets into source. iOS now uses syntactically valid bundle and App Group identifiers. GitHub Actions builds/tests Android and the backend, compiles the shared iOS arm64 framework, and builds an unsigned generic iOS device app without launching an iOS simulator.

**Remaining:** configure/register the iOS App Group and signing team, verify release signing with private CI secrets, complete platform accessibility and device checks, and run the GitHub workflow after publishing the branch. Android Room migration instrumentation now covers both the full schema v1→v12 path and preservation/default values for v11→v12 on an Android emulator.

**Acceptance:** testing gates in `../testing/testing-strategy.md` pass on supported toolchains; docs match shipped behavior; no demo data is presented as user data.

## Dependency order and risks

The selected Room/KSP/Koin/Ktor libraries compile for Android and iOS device architecture. Remaining risks include full navigation back-stack parity beyond the defined Back behavior, registered Apple signing capabilities, notification/widget workflows on physical devices, broader screen-reader and 200% text review, and deploying the optional AI backend. Keep additions in the version catalog and recheck compatibility when upgrading. The GitHub Actions workflow is green on the current main branch; signing and deployment requirements remain external release gates.

# Implementation roadmap

This sequence starts from the repository's current starter state: Compose Multiplatform shell only, with no persistence, navigation, DI, AI, or product flows.

## Phase 0 — Compatibility spike and app foundation

**Work:** validate requested Room KMP, Koin, Ktor, serialization, coroutines, and navigation versions against Kotlin 2.4.20 / AGP 9.1.1 / CMP 1.12.1; pin versions; replace starter greeting with tokenized theme, app root, tab navigation and startup state. Add representative preview scaffolds.

**Acceptance:** Android and iOS shared app compile; five destinations preserve state; back behavior is defined on both; theme switch renders light/dark/system; no unsupported dependency remains speculative.

## Phase 1 — Domain and local persistence

**Work:** domain types, repository contracts, Room schema/entities/DAOs/mappers, preferences, DI modules, fake clock, note/task/subtask/session storage.

**Acceptance:** local CRUD and Flow observation work offline; atomic selected-suggestion write API exists; migration policy and tests are in place.

## Phase 2 — Core manual productivity flows

**Work:** Today, Inbox, editor, task list/details, planner order, completion/undo and empty/error states.

**Acceptance:** user can capture a note and create/edit/complete/reorder tasks with no network; state is reflected across entry points; accessibility and large-text behavior reviewed.

## Phase 3 — Focus and explainable insights

**Work:** persisted timestamp timer transitions, optional task linkage, history, derived metrics and settings/privacy controls.

**Acceptance:** timer survives backgrounding/recreation and uses timestamps; abandon/discard behavior is clear; each metric matches documented records and period semantics.

## Phase 4 — AI contracts and deterministic mock

**Work:** versioned DTOs, validation/normalization, provider abstraction, mock scenarios, extraction/breakdown/planning review state, explicit confirmation.

**Acceptance:** all mock scenarios are deterministic; invalid/partial/duplicate output recovers safely; no proposal persists before selected confirmation; existing subtasks remain untouched.

## Phase 5 — Secure backend integration

**Work:** Ktor remote adapter, backend contract/configuration, error mapping, privacy copy, request limits and observable provider status. Backend service itself is a separate project/deployment decision and is not part of this client repository phase.

**Acceptance:** app contains no provider secret; backend owns credentials and model selection; offline/timeout/rate-limit/retry paths preserve input; only explicit AI action transmits note content.

## Phase 6 — Polish and release readiness

**Work:** visual verification on Android/iOS, accessibility audits, localization/date testing, performance, crash-safe persistence, release build configuration and real version/project metadata.

**Acceptance:** testing gates in `../testing/testing-strategy.md` pass on supported toolchains; docs match shipped behavior; no demo data is presented as user data.

## Dependency order and risks

Phase 0 is a gate: declared versions are unusually forward-dated/early relative to common stable stacks, and compatibility of requested KMP libraries is unverified in this repository. Room KMP compiler/plugin alignment, iOS database driver, navigation back-stack parity, Android API 37 availability in CI, and Xcode/Gradle environment are risks. Keep all additions in the version catalog; do not raise or lower versions without a reproducible compatibility reason. The repository currently has no Git metadata, so establish version control before implementation work that needs reviewable commits.

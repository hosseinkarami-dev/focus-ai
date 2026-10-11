# Testing strategy

Automated coverage now includes backend health, response-envelope/normalization, invalid schema, and rate-limit unit cases. Android host tests, Android debug assembly, shared iOS arm64 compilation, and a generic iPhoneOS app-plus-widget build have passed. These compile checks do not replace device-level usability and accessibility verification. Keep business logic deterministic with injected clock, ID generator where helpful, repositories, and provider as production flows are added.

## Unit tests

- Use cases: note save/edit, task CRUD/completion, recurring occurrence generation and Undo, month-end recurrence dates, reminder scheduling/cancellation and timezone changes, plan membership/order, confirmation writes only selected suggestions, breakdown appends without replacing existing steps, date interpretation, and timer transitions.
- AI validation: every schema/version/enum/date/duration/length/partial/duplicate/unknown-ID case in `ai-contracts.md`.
- Insights and daily goals: period boundaries, local-day/timezone semantics, completed and paused sessions, active timer progress, average duration, and empty periods.
- Error mapping: cancellation distinct from error; retries preserve note and never persist proposals.

## Repository and database tests

- Test Room entities/mappers and DAO flows, ordering, archive behavior, atomic confirmation, and clear-data behavior against an in-memory or temporary platform database.
- Verify local operations require no network and emit updated Flow values promptly.
- Android instrumentation migration tests create and upgrade a v1 fixture through v12 and verify v11 settings are preserved while v12 defaults are applied. Keep the fixtures and runtime checks current before schema changes are released.

## Provider/network tests

- Mock provider scenarios are deterministic: success, empty, delay, offline, timeout, malformed, partial, duplicates, cancellation.
- Test remote DTO serialization and Ktor request/response mapping with an in-process/mock engine; assert no provider credential or note text enters logs.
- Test unsupported version, oversized payload, HTTP rate limit, backend unavailable, and malformed JSON.

## State holder/ViewModel tests

- Verify initial/loading/content/empty/error/offline states and event-to-use-case wiring.
- Ensure duplicate Analyze taps launch one request, cancellation restores editable state, retry uses preserved text, and stale completion cannot overwrite newer edits.
- Ensure selected/rejected/edited suggestions remain draft until explicit confirmation and only selected items persist.

## Compose/UI and platform tests

- Semantic tests for labels, selection, task completion/undo, confirmations, editor validation, empty/error copy, and navigation return paths.
- Android and iOS device checks for safe areas, keyboard insets, system back/sheet dismissal, tab state restoration, notification permission/denial, task and break reminder delivery, larger font sizes, screen reader order, and minimum touch targets.
- Timer lifecycle test: start, background, pause/resume, process recreation, app relaunch, finish, and discard. Compare against a fake clock in unit tests and real lifecycle behavior on each platform.
- Visual review Today, Inbox, AI Review, Task Details, Focus across light/dark and compact/wide viewport. No screenshot workflow currently exists in the repository.
- Verify Android and iOS home-screen widgets refresh from current open tasks, render empty/long-title states, and route their task, task-creation, and capture actions into the matching app screens. Check the iOS App Group entitlement on both targets in a signed device build.
- Review at 200% text size and with screen readers: Today, Inbox, Tasks List/Week, task editor, Focus, Settings, dialogs, and transient feedback. Confirm all content remains reachable by scrolling, controls retain a 48 dp minimum target, dynamic labels describe actions and state, and no fixed-size chart, timer, or date cell clips text.

The current checkout was verified with Android host tests, Android debug and release assembly, backend API tests, Android emulator Room migrations, shared iOS arm64 compilation, and an unsigned generic iPhoneOS app-plus-widget build. Android visual checks at 150% and 200% font scale confirmed task metadata wraps; the bottom navigation now keeps labels on one line and the add action becomes compact at very large font sizes. Notification delivery, widget behavior, screen-reader navigation, and signed iOS device behavior remain pending. iOS simulator verification is intentionally not part of this workflow.

## Automated CI coverage

- GitHub Actions Android job: `:shared:testAndroidHostTest`, `:androidApp:assembleDebug`, and Room instrumentation migration tests on an API 35 emulator; the separate Ktor service runs its API tests.
- GitHub Actions iOS job: shared iOS arm64 compilation and unsigned generic iOS device build only; no iOS simulator is launched.

## Integration and release gates

1. Android shared unit/host tests and Android app assembly pass.
2. Shared iOS arm64 compilation and unsigned generic iOS app/widget build pass; signed device workflow is verified before store distribution.
3. End-to-end local workflow passes with network disabled.
4. AI integration proves untrusted payload validation and explicit-confirmation persistence boundary.
5. Accessibility and theme review has no blocking contrast, clipping, or semantics defects.
6. Dependency compatibility and Room migration plan are recorded with the actual tested version matrix.

Do not treat a green compile as proof of the product workflows or timer lifecycle.

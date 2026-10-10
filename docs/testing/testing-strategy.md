# Testing strategy

No new automated tests have been added yet. Android debug assembly and shared iOS simulator source compilation have been run successfully for the current foundation. Keep business logic deterministic with injected clock, ID generator where helpful, repositories, and provider as production flows are added.

## Unit tests

- Use cases: note save/edit, task CRUD/completion, plan membership/order, confirmation writes only selected suggestions, breakdown appends without replacing existing steps, date interpretation, and timer transitions.
- AI validation: every schema/version/enum/date/duration/length/partial/duplicate/unknown-ID case in `ai-contracts.md`.
- Insights: period boundaries, timezone/date semantics, completed tasks, paused/abandoned sessions, average duration, and empty periods.
- Error mapping: cancellation distinct from error; retries preserve note and never persist proposals.

## Repository and database tests

- Test Room entities/mappers and DAO flows, ordering, archive behavior, atomic confirmation, and clear-data behavior against an in-memory or temporary platform database.
- Verify local operations require no network and emit updated Flow values promptly.
- Test database migration paths before schema changes are released; include migration fixtures.

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
- Android and iOS simulator/device checks for safe areas, keyboard insets, system back/sheet dismissal, tab state restoration, larger font sizes, screen reader order, and minimum touch targets.
- Timer lifecycle test: start, background, pause/resume, process recreation, app relaunch, finish, and discard. Compare against a fake clock in unit tests and real lifecycle behavior on each platform.
- Visual review Today, Inbox, AI Review, Task Details, Focus across light/dark and compact/wide viewport. No screenshot workflow currently exists in the repository.

## Integration and release gates

1. Android shared unit/host tests and Android app assembly pass.
2. iOS simulator shared tests and Xcode app build pass on a configured Apple toolchain.
3. End-to-end local workflow passes with network disabled.
4. AI integration proves untrusted payload validation and explicit-confirmation persistence boundary.
5. Accessibility and theme review has no blocking contrast, clipping, or semantics defects.
6. Dependency compatibility and Room migration plan are recorded with the actual tested version matrix.

Do not treat a green compile as proof of the product workflows or timer lifecycle.

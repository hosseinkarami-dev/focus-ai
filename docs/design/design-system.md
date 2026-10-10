# FocusAI design system

## Direction

Use a quiet, editorial productivity visual language: warm lilac tinted canvas, deep indigo as the action color, clear typography, thin separators, and compact task rows. Apricot provides a warm secondary accent. The visual hierarchy comes from spacing and type rather than badges everywhere or large dashboard cards. AI content uses a labeled “Suggested” treatment with an icon and wording so status never depends on color alone.

## Color tokens

Centralize these semantic tokens in `ui/theme`; these are the encoded light and dark palettes. Validate contrast in implementation, including disabled and large-text contexts.

| Token | Light | Dark | Use |
|---|---|---|---|
| `background` | `#F5F4FA` | `#141320` | Warm lilac / ink canvas |
| `surface` | `#FFFEFF` | `#1D1B2A` | Sheets and contained surfaces |
| `surfaceVariant` | `#ECEBF3` | `#292737` | Quiet supporting surfaces |
| `primary` | `#5954C8` | `#B9B4FF` | Indigo actions and navigation |
| `onPrimary` | `#FFFFFF` | `#211B5E` | Primary action text/icons |
| `textPrimary` | `#201F2D` | `#E8E6F3` | Titles and body |
| `textSecondary` | `#64627A` | `#C7C4D8` | Supporting copy |
| `outline` | `#777487` | `#918EA3` | Borders and dividers |
| `tertiary` | `#D66B55` | `#FFB49C` | Apricot accent |
| `error` | `#B33D36` | `#FFB4AB` | Invalid/destructive |
| `priorityHigh` | `#B33D36` | `#FFB4AB` | High priority plus “High” label |
| `priorityMedium` | `#8A5A11` | `#F0BE68` | Medium priority plus label |
| `priorityLow` | `#356883` | `#91C9E4` | Low priority plus label |

The FocusAI mark is a soft white focus ring with four alignment ticks and a lilac orbit over indigo, with an apricot center. Source: [`focusai-mark.svg`](assets/focusai-mark.svg); app icons and the Compose mark use the same geometry. iOS uses this mark on its launch storyboard. Android uses a reduced splash rendition that keeps every tick inside the system safe area.

Use Material 3 `ColorScheme` semantic roles where applicable. Priority chips include text; completion includes checkbox state and accessible announcement. Never use hue alone to convey urgency or AI uncertainty.

## Typography

Use OFL-licensed Manrope for Latin interfaces and Noto Sans Arabic for Arabic and Persian. The font binaries live in shared Compose resources and license files are retained under `assets/fonts`. Define semantic Material 3 styles centrally and allow font scaling.

| Role | Target |
|---|---|
| Screen title | 28sp, semibold, line height 34sp |
| Section title | 20sp, semibold, line height 26sp |
| Task title/body | 16sp, regular/medium, line height 24sp |
| Supporting text | 14sp, regular, line height 20sp |
| Metadata/caption | 12sp, medium, line height 16sp; never the sole carrier of meaning |
| Timer digits | 48sp minimum, tabular figures where supported |

## Layout and shape tokens

- Base spacing: 4dp; scale: 4, 8, 12, 16, 24, 32, 40.
- Screen horizontal inset: 20dp compact, up to 28dp on wider phones; content max width 640dp when the app is used on larger displays.
- Tap target: at least 48dp square for controls; preserve visible icon size independently.
- Corners: 8dp controls, 12dp fields and task containers, 20dp sheets/dialogs.
- Icons: 20dp inline, 24dp navigation/action, 40dp empty-state illustration maximum.
- Dividers 1dp; shadows are rare and low elevation. Prefer outline and tonal surface separation.
- Respect safe insets and IME insets. Scroll focused inputs into view and keep primary actions reachable.

## Components

- Buttons: filled for one primary action; tonal/outlined for secondary; text for low-emphasis links. Loading replaces label with a small indicator and disables repeat submission.
- Fields: labeled multiline note field, persistent text, clear character guidance near the actual limit, and inline validation.
- Task row: 48dp+ interaction targets, completion control, title, compact metadata line, priority word/marker, optional date and estimate. Entire row opens details; completion button does not trigger row navigation.
- Suggestion panel: explicit “AI suggestion” label, per-field edit controls, uncertainty note beside the affected field, select checkbox, reject action. No provider-specific branding.
- Confirmation: review count and action consequence; selected tasks save only after explicit confirmation. Destructive remove/archive offers undo when safe; permanent data clear asks for confirmation.
- Bottom navigation: five labeled destinations with selected semantics; preserve each destination's scroll/query state.
- Feedback: branded toast-style snackbar for undoable changes; inline error for recoverable network/validation failures with retry where meaningful.

## Themes and accessibility

System theme is default. Settings can choose Light, Dark, or System and the selected setting persists in Room. English, Persian, Spanish, French, and Arabic are available; Persian and Arabic use right-to-left layout. The focus length is editable (15, 25, 45, or 60 minutes) and shown on the Focus screen. Map tokens into light and dark Material 3 schemes; do not use dynamic color as the product identity. Verify contrast against WCAG AA targets for normal text (4.5:1) and large text/UI indicators (3:1); test actual rendered colors.

Every icon-only control has a semantic label, state, and action. Keep focus order aligned with visual order, announce completion and timer state changes, support font scaling without clipping, use content descriptions for meaningful imagery, and honor reduced-motion preferences. Empty illustrations are supplemental; pair them with concise text and an action.

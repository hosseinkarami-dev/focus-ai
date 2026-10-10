# FocusAI design system

## Direction

Use a quiet, editorial productivity visual language: warm neutral canvas, deep pine as the action color, clear typography, thin separators, and compact task rows. The visual hierarchy comes from spacing and type rather than gradients, badges everywhere, or large dashboard cards. AI content uses a labeled “Suggested” treatment with an icon and wording so status never depends on color alone.

## Color tokens

Centralize these semantic tokens in `ui/theme`; the following hex values are design targets to encode once, not scattered literals. Validate contrast in implementation, including disabled and large-text contexts.

| Token | Light | Dark | Use |
|---|---|---|---|
| `background` | `#F7F7F2` | `#151916` | App canvas |
| `surface` | `#FFFFFF` | `#202622` | Sheets and contained surfaces |
| `surfaceVariant` | `#EEF1EC` | `#29312C` | Quiet supporting surfaces |
| `primary` | `#245B48` | `#9BD2B4` | Main actions, selected navigation |
| `onPrimary` | `#FFFFFF` | `#10271D` | Primary action text/icons |
| `textPrimary` | `#18211C` | `#F0F3EF` | Titles and body |
| `textSecondary` | `#59645D` | `#B4BEB6` | Supporting copy |
| `outline` | `#D7DED8` | `#3A443D` | Borders and dividers |
| `success` | `#28734D` | `#85D3A4` | Completed/saved state |
| `warning` | `#8A5A11` | `#F0BE68` | Overdue/attention |
| `error` | `#A33E36` | `#F2A29B` | Invalid/destructive |
| `information` | `#356883` | `#91C9E4` | Connectivity/help |
| `priorityHigh` | `#A33E36` | `#F2A29B` | High priority plus “High” label |
| `priorityMedium` | `#8A5A11` | `#F0BE68` | Medium priority plus label |
| `priorityLow` | `#356883` | `#91C9E4` | Low priority plus label |

Use Material 3 `ColorScheme` semantic roles where applicable. Priority chips include text; completion includes checkbox state and accessible announcement. Never use hue alone to convey urgency or AI uncertainty.

## Typography

Use the platform sans-serif default initially to preserve legibility and avoid bundling a font before branding validation. Define semantic Material 3 styles centrally and allow font scaling.

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
- Feedback: snackbar for undoable changes; inline error for recoverable network/validation failures with retry where meaningful.

## Themes and accessibility

System theme is default. Settings can choose Light, Dark, or System and the selected setting persists locally. Map tokens into light and dark Material 3 schemes; do not use dynamic color as the product identity. Verify contrast against WCAG AA targets for normal text (4.5:1) and large text/UI indicators (3:1); test actual rendered colors.

Every icon-only control has a semantic label, state, and action. Keep focus order aligned with visual order, announce completion and timer state changes, support font scaling without clipping, use content descriptions for meaningful imagery, and honor reduced-motion preferences. Empty illustrations are supplemental; pair them with concise text and an action.

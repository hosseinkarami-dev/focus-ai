import SwiftUI
import WidgetKit

private let appGroup = "group.io.github.hosseinkarami_dev.focusai"
private let taskSnapshotKey = "focusai.openTaskTitles"

private struct FocusEntry: TimelineEntry {
    let date: Date
    let tasks: [String]
}

private struct FocusProvider: TimelineProvider {
    func placeholder(in context: Context) -> FocusEntry {
        FocusEntry(date: .now, tasks: ["Plan your day", "Take a mindful break"])
    }

    func getSnapshot(in context: Context, completion: @escaping (FocusEntry) -> Void) {
        completion(FocusEntry(date: .now, tasks: readTasks()))
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<FocusEntry>) -> Void) {
        let entry = FocusEntry(date: .now, tasks: readTasks())
        completion(Timeline(entries: [entry], policy: .after(.now.addingTimeInterval(15 * 60))))
    }

    private func readTasks() -> [String] {
        let titles = UserDefaults(suiteName: appGroup)?.string(forKey: taskSnapshotKey)?
            .components(separatedBy: "\n")
            .filter { !$0.isEmpty } ?? []
        return Array(titles.prefix(3))
    }
}

private struct FocusWidgetView: View {
    @Environment(\.widgetFamily) private var family
    let entry: FocusEntry

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(spacing: 7) {
                Image(systemName: "sparkle")
                    .font(.system(size: 13, weight: .bold))
                    .foregroundStyle(Color(red: 0.38, green: 0.34, blue: 0.92))
                Text("FocusAI")
                    .font(.system(.headline, design: .rounded).weight(.bold))
                Spacer(minLength: 0)
                Link(destination: URL(string: "focusai://task/new")!) {
                    Image(systemName: "plus.circle.fill")
                        .font(.system(size: 21))
                        .foregroundStyle(Color(red: 0.38, green: 0.34, blue: 0.92))
                }
            }

            if family == .systemSmall {
                Spacer(minLength: 0)
                Text(entry.tasks.first ?? "A clear mind starts with one small step.")
                    .font(.system(.subheadline, design: .rounded).weight(.medium))
                    .lineLimit(3)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Spacer(minLength: 0)
                Link(destination: URL(string: "focusai://tasks")!) {
                    Label("Open tasks", systemImage: "arrow.up.right")
                        .font(.caption.weight(.semibold))
                }
            } else {
                if entry.tasks.isEmpty {
                    Text("Your next focused moment starts here.")
                        .font(.system(.subheadline, design: .rounded))
                        .foregroundStyle(.secondary)
                        .lineLimit(2)
                } else {
                    ForEach(Array(entry.tasks.enumerated()), id: \.offset) { index, title in
                        HStack(spacing: 8) {
                            Circle()
                                .fill(Color(red: 0.38, green: 0.34, blue: 0.92).opacity(0.75))
                                .frame(width: 6, height: 6)
                            Text(title)
                                .font(.system(.subheadline, design: .rounded))
                                .lineLimit(1)
                            Spacer(minLength: 0)
                            if index == 0 {
                                Link(destination: URL(string: "focusai://inbox/capture")!) {
                                    Image(systemName: "square.and.pencil")
                                        .font(.caption.weight(.semibold))
                                        .foregroundStyle(.secondary)
                                }
                            }
                        }
                    }
                }
                Spacer(minLength: 0)
                HStack {
                    Link("View tasks", destination: URL(string: "focusai://tasks")!)
                    Spacer()
                    Link("Capture thought", destination: URL(string: "focusai://inbox/capture")!)
                }
                .font(.caption.weight(.semibold))
                .foregroundStyle(Color(red: 0.38, green: 0.34, blue: 0.92))
            }
        }
        .padding()
        .containerBackground(.background, for: .widget)
        .widgetURL(URL(string: "focusai://tasks"))
    }
}

@main
struct FocusAIWidget: Widget {
    let kind = "FocusAIWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: FocusProvider()) { entry in
            FocusWidgetView(entry: entry)
        }
        .configurationDisplayName("FocusAI")
        .description("Keep your next few priorities close and capture thoughts quickly.")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

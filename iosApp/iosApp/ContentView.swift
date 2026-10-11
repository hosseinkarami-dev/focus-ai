import UIKit
import SwiftUI
import WidgetKit
import Shared

struct ComposeView: UIViewControllerRepresentable {
    let initialAction: String?

    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController(initialAction: initialAction)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView: View {
    @ObservedObject var quickActions: QuickActionRouter
    @Environment(\.scenePhase) private var scenePhase

    var body: some View {
        ComposeView(initialAction: quickActions.action)
            .id(quickActions.revision)
            .ignoresSafeArea()
            .onOpenURL { url in
                let action: String
                switch url.host {
                case "task": action = "io.github.hosseinkarami_dev.focusai.ADD_TASK"
                case "inbox": action = "io.github.hosseinkarami_dev.focusai.CAPTURE_NOTE"
                default: action = "io.github.hosseinkarami_dev.focusai.OPEN_TASKS"
                }
                quickActions.open(action)
            }
            .onChange(of: scenePhase) { _, phase in
                if phase == .background { WidgetCenter.shared.reloadAllTimelines() }
            }
    }
}

import SwiftUI
import UIKit

@MainActor
final class QuickActionRouter: ObservableObject {
    @Published var action: String?
    @Published var revision = 0

    func open(_ shortcutType: String) {
        action = shortcutType
        revision += 1
    }
}

@MainActor
final class AppDelegate: NSObject, UIApplicationDelegate {
    let quickActions = QuickActionRouter()

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        if let shortcut = launchOptions?[.shortcutItem] as? UIApplicationShortcutItem {
            quickActions.open(shortcut.type)
        }
        return true
    }

    func application(
        _ application: UIApplication,
        performActionFor shortcutItem: UIApplicationShortcutItem,
        completionHandler: @escaping (Bool) -> Void
    ) {
        quickActions.open(shortcutItem.type)
        completionHandler(true)
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate

    var body: some Scene {
        WindowGroup {
            ContentView(quickActions: appDelegate.quickActions)
        }
    }
}

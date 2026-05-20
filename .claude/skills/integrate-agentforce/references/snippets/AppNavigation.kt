// No-op Navigation implementation. Replace with real navigation logic
// when you want the agent to open records or app screens — e.g. parse
// the URL and dispatch to your Navigation Compose graph or Activity.

package {{PACKAGE}}.agentforce

import com.salesforce.android.mobile.interfaces.navigation.Navigation

class AppNavigation : Navigation {

    override fun navigate(url: String) {
        // TODO: handle navigation requests from the agent.
        // Example: parse the URL and dispatch to your nav graph,
        // or fall through to a Custom Tabs intent.
    }
}

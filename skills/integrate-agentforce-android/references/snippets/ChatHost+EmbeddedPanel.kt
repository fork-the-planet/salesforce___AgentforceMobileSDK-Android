// Embedded panel host. Place the chat container inline in an existing
// screen — for example, alongside a list view in a tablet-style layout.

package {{PACKAGE}}.agentforce

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AgentforceEmbeddedHost(
    holder: AgentforceHolder,
    leftContent: @Composable () -> Unit
) {
    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) { leftContent() }
        Box(Modifier.weight(1f)) {
            holder.conversation?.let { conv ->
                holder.client.AgentforceConversationContainer(
                    conversation = conv,
                    onClose = {
                        // Embedded panel — there's nothing to close to.
                        // Either no-op, or hide via your screen's state.
                    }
                )
            }
        }
    }
}

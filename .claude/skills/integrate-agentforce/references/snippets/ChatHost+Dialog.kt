// Dialog/modal popup chat presentation. Use for short-lived, focused
// interactions where the chat shouldn't take over the whole screen.

package {{PACKAGE}}.agentforce

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AgentforceDialogHost(
    holder: AgentforceHolder,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            holder.conversation?.let { conv ->
                holder.client.AgentforceConversationContainer(
                    conversation = conv,
                    onClose = onDismiss
                )
            }
        }
    }
}

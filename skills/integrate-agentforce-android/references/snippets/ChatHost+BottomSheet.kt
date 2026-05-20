// Bottom-sheet chat presentation. Open with a button; close by swiping
// down or tapping the SDK's built-in close button.

package {{PACKAGE}}.agentforce

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentforceChatHost(holder: AgentforceHolder) {
    var showChat by rememberSaveable { mutableStateOf(false) }

    Button(onClick = { showChat = true }) {
        Text("Ask the agent")
    }

    if (showChat) {
        ModalBottomSheet(onDismissRequest = { showChat = false }) {
            holder.conversation?.let { conv ->
                holder.client.AgentforceConversationContainer(
                    conversation = conv,
                    onClose = { showChat = false }
                )
            }
        }
    }
}

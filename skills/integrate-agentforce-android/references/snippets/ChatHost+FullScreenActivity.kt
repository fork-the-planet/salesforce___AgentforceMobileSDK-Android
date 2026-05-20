// Full-screen Activity host. Use when the chat is the primary task and
// shouldn't be dismissed by an accidental swipe.
//
// Don't forget to declare this Activity in AndroidManifest.xml:
//
//   <activity android:name=".agentforce.AgentforceChatActivity"
//             android:exported="false" />

package {{PACKAGE}}.agentforce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import {{PACKAGE}}.MyApp

class AgentforceChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val holder = (application as MyApp).agentforce

        setContent {
            holder.conversation?.let { conv ->
                holder.client.AgentforceConversationContainer(
                    conversation = conv,
                    onClose = { finish() }
                )
            }
        }
    }
}

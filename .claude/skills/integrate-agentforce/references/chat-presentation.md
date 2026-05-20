# Chat presentation reference (Android)

`AgentforceClient.AgentforceConversationContainer` is a `@Composable`. Drop it inside any Compose scope. The four common patterns:

## Bottom sheet (recommended default)

A modal that slides up from the bottom. Best for "open the assistant" CTAs anywhere in the app.

```kotlin
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
```

## Full-screen Activity / Compose route

Use when the chat is the primary task. Either a dedicated `Activity` or a route inside a `NavHost`:

```kotlin
class ChatActivity : ComponentActivity() {
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
```

…or as a Navigation Compose route:

```kotlin
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen(onAskAgent = { navController.navigate("chat") }) }
    composable("chat") {
        holder.conversation?.let { conv ->
            holder.client.AgentforceConversationContainer(
                conversation = conv,
                onClose = { navController.popBackStack() }
            )
        }
    }
}
```

## Embedded panel

Place the chat container inline in an existing screen — for example, alongside a list view in a tablet-style layout.

```kotlin
@Composable
fun TabletScreen(holder: AgentforceHolder) {
    Row(Modifier.fillMaxSize()) {
        ProductList(modifier = Modifier.weight(1f))
        Box(Modifier.weight(1f)) {
            holder.conversation?.let { conv ->
                holder.client.AgentforceConversationContainer(
                    conversation = conv,
                    onClose = { /* embedded — no-op or hide */ }
                )
            }
        }
    }
}
```

## Dialog / modal popup

For short-lived, focused interactions:

```kotlin
@Composable
fun AgentforceDialog(holder: AgentforceHolder, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)) {
            holder.conversation?.let { conv ->
                holder.client.AgentforceConversationContainer(
                    conversation = conv,
                    onClose = onDismiss
                )
            }
        }
    }
}
```

## Persisting `showChat` across rotation

Always use `rememberSaveable` for the visibility flag — `remember` alone resets on configuration change. The conversation itself survives rotation because it's owned by `AgentforceHolder` on `Application`, not by the composable.

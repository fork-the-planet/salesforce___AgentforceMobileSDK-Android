// AgentforceHolder.kt
//
// Owns the AgentforceClient and the active AgentforceConversation.
// Must be retained at the Application level (or as a Hilt @Singleton)
// so the conversation isn't dropped when the host Activity is recreated
// on rotation.
//
// This template covers all three modes — the skill should delete the
// branches that don't apply to the chosen flow.

package {{PACKAGE}}.agentforce

import android.app.Application
import com.salesforce.android.agentforcesdkimpl.AgentforceClient
import com.salesforce.android.agentforcesdkimpl.AgentforceConversation
import com.salesforce.android.agentforcesdkimpl.configuration.AgentforceConfiguration
import com.salesforce.android.agentforcesdkimpl.configuration.AgentforceMode
import com.salesforce.android.agentforcesdkimpl.configuration.ServiceAgentConfiguration
import com.salesforce.android.agentforceservice.miaw.AuthorizationContext
import com.salesforce.android.agentforceservice.miaw.AuthorizationMethod

class AgentforceHolder(application: Application) {

    val client: AgentforceClient = AgentforceClient()
    var conversation: AgentforceConversation? = null
        private set

    init {
        // ── EMPLOYEE AGENT ──────────────────────────────────────────────
        // Keep this branch for OAuth or OrgJWT employee flows.
        //
        // val configuration = AgentforceConfiguration.builder(
        //     authCredentialProvider = AppCredentialProvider()
        // )
        //     .setApplication(application)
        //     .setSalesforceDomain("{{SALESFORCE_DOMAIN}}")
        //     .setNetwork(AppNetwork())
        //     .setNavigation(AppNavigation())
        //     .setLogger(AppLogger())
        //     .setDelegate(AppAgentforceUIDelegate())
        //     .build()
        //
        // val mode = AgentforceMode.FullConfig(configuration)
        //
        // client.init(
        //     authCredentialProvider = configuration.authCredentialProvider,
        //     agentforceMode = mode,
        //     application = application
        // )
        //
        // conversation = client.startAgentforceConversation(agentId = "{{AGENT_ID}}")

        // ── PUBLIC SERVICE AGENT ────────────────────────────────────────
        // Keep this branch for the MIAW service-agent flow.
        //
        // val configuration = AgentforceConfiguration.builder(
        //     authCredentialProvider = AppCredentialProvider() // returns Guest(salesforceDomain)
        // )
        //     .setApplication(application)
        //     .setSalesforceDomain("{{SALESFORCE_DOMAIN}}")
        //     .setNetwork(AppNetwork())
        //     .setNavigation(AppNavigation())
        //     .setLogger(AppLogger())
        //     .setDelegate(AppAgentforceUIDelegate())
        //     .build()
        //
        // val serviceConfig = ServiceAgentConfiguration.builder(
        //     serviceApiURL = "{{SERVICE_API_URL}}",
        //     organizationId = "{{ORG_ID}}",
        //     esDeveloperName = "{{ES_DEVELOPER_NAME}}",
        //     authorizationContext = AuthorizationContext(
        //         authorizationMethod = AuthorizationMethod.UNVERIFIED
        //     )
        // ).build()
        //
        // val mode = AgentforceMode.ServiceAgent(
        //     serviceAgentConfiguration = serviceConfig,
        //     agentforceConfiguration = configuration
        // )
        //
        // client.init(
        //     authCredentialProvider = configuration.authCredentialProvider,
        //     agentforceMode = mode,
        //     application = application
        // )
        //
        // conversation = client.startAgentforceServiceConversation(
        //     esDeveloperName = serviceConfig.esDeveloperName
        // )
    }
}

// Template: AppCredentialProvider for Guest URL
//
// Use this for service-agent flows (the SDK still requires an
// AgentforceAuthCredentialProvider; for unauthenticated public agents,
// the canonical pattern returns Guest(salesforceDomain)).
//
// Or use this for non-service-agent flows where the agent is publicly
// accessible via the Agent API behind an Experience Cloud site.

package {{PACKAGE}}.agentforce

import com.salesforce.android.agentforceservice.AgentforceAuthCredentialProvider
import com.salesforce.android.agentforceservice.AgentforceAuthCredentials

class AppCredentialProvider(
    private val salesforceDomain: String = "{{SALESFORCE_DOMAIN}}"
) : AgentforceAuthCredentialProvider {

    override fun getAuthCredentials(): AgentforceAuthCredentials {
        return AgentforceAuthCredentials.Guest(url = salesforceDomain)
    }

    override suspend fun fetchMIAWJWTForPassthrough(
        pathOrUrl: String,
        serviceApiUrl: String,
        esDeveloperName: String
    ): AgentforceAuthCredentials? = null

    override suspend fun getIdentityToken(): String? = null
}

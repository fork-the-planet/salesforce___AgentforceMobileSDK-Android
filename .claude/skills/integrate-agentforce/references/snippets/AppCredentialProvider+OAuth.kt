// Template: AppCredentialProvider for Salesforce Mobile SDK / OAuth
//
// The skill should write this file as `AppCredentialProvider.kt`.
// Replace the body of `getAuthCredentials()` with whatever pulls the
// current OAuth token, org ID, and user ID from your app's auth state.
//
// If the consumer uses Salesforce Mobile SDK, the canonical source is
// `SalesforceSDKManager.getInstance().userAccountManager.currentUser`.

package {{PACKAGE}}.agentforce

import com.salesforce.android.agentforceservice.AgentforceAuthCredentialProvider
import com.salesforce.android.agentforceservice.AgentforceAuthCredentials
import com.salesforce.androidsdk.app.SalesforceSDKManager

/**
 * Supplies OAuth credentials to the Agentforce SDK on every request.
 *
 * `getAuthCredentials()` is invoked for each authenticated SDK call, so
 * always return the *current* token — do not cache the value at init.
 */
class AppCredentialProvider : AgentforceAuthCredentialProvider {

    override fun getAuthCredentials(): AgentforceAuthCredentials {
        // TODO: Replace with your real auth source. Example for Salesforce Mobile SDK:
        val account = SalesforceSDKManager.getInstance().userAccountManager.currentUser
            ?: error("No authenticated Salesforce user — show login before opening Agentforce")

        return AgentforceAuthCredentials.OAuth(
            authToken = account.authToken,
            orgId = account.orgId,
            userId = account.userId
        )
    }

    /**
     * Required by the interface for service-agent passthrough flows.
     * Return null for OAuth-only employee integrations.
     */
    override suspend fun fetchMIAWJWTForPassthrough(
        pathOrUrl: String,
        serviceApiUrl: String,
        esDeveloperName: String
    ): AgentforceAuthCredentials? = null

    override suspend fun getIdentityToken(): String? {
        return (getAuthCredentials() as? AgentforceAuthCredentials.OAuth)?.authToken
    }
}

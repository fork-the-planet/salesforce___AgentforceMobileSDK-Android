// Template: AppCredentialProvider for Org JWT
//
// The skill should write this file as `AppCredentialProvider.kt`.
// `getAuthCredentials()` must return the *current* JWT every time it's
// called — JWTs expire, and the SDK does not refresh them.

package {{PACKAGE}}.agentforce

import com.salesforce.android.agentforceservice.AgentforceAuthCredentialProvider
import com.salesforce.android.agentforceservice.AgentforceAuthCredentials

/**
 * Supplies a Salesforce-signed Org JWT to the Agentforce SDK on every request.
 */
class AppCredentialProvider(
    /**
     * Function that returns the latest JWT. Call out to your backend, encrypted
     * SharedPreferences, or whatever stores the token — do not cache the value
     * here, JWTs expire.
     */
    private val jwtSource: () -> String
) : AgentforceAuthCredentialProvider {

    override fun getAuthCredentials(): AgentforceAuthCredentials {
        return AgentforceAuthCredentials.OrgJWT(orgJWT = jwtSource())
    }

    override suspend fun fetchMIAWJWTForPassthrough(
        pathOrUrl: String,
        serviceApiUrl: String,
        esDeveloperName: String
    ): AgentforceAuthCredentials? = null

    override suspend fun getIdentityToken(): String? = jwtSource()
}

# Auth flow reference (Android)

Pick the auth flow based on the use case. **Don't** lead with the credential type — most consumers won't know which one applies.

## The three `AgentforceMode` cases

```kotlin
sealed class AgentforceMode {
    data class FullConfig(val agentforceConfiguration: AgentforceConfiguration) : AgentforceMode()
    data class ServiceAgent(
        val serviceAgentConfiguration: ServiceAgentConfiguration,
        val agentforceConfiguration: AgentforceConfiguration? = null
    ) : AgentforceMode()
    data class EmployeeAgent(
        val employeeAgentConfiguration: EmployeeAgentConfiguration,
        val agentforceConfiguration: AgentforceConfiguration? = null
    ) : AgentforceMode()
}
```

- `FullConfig` — most common. Pass an `AgentforceConfiguration` built via `AgentforceConfiguration.builder(authCredentialProvider).setApplication(...).setSalesforceDomain(...).build()`. Works for both signed-in employee agents and the Voice path.
- `ServiceAgent` — public, customer-facing service agent (MIAW deployment). Always pair with an `AgentforceConfiguration` that provides theme/logger/network.
- `EmployeeAgent` — newer streamlined builder. Useful when you want `EmployeeAgentConfiguration.builder(user, forceConfigEndpoint)` defaults. The README's "Option A: Full UI" path uses `FullConfig` instead.

## The four `AgentforceAuthCredentials` cases

```kotlin
sealed class AgentforceAuthCredentials {
    data class OAuth(val authToken: String, val orgId: String, val userId: String) : AgentforceAuthCredentials()
    data class OrgJWT(val orgJWT: String) : AgentforceAuthCredentials()
    data class Guest(val url: String) : AgentforceAuthCredentials()
    data class PassThroughAuth(val miawJWT: String, val eventID: String? = null) : AgentforceAuthCredentials()
}
```

`getAuthCredentials()` is called on every authenticated request, so return live values — don't cache a stale token. The SDK does not refresh tokens for you.

## Decision tree

```
Is the agent customer-facing (anyone can chat without signing in)?
├── Yes → AgentforceMode.ServiceAgent + MIAW deployment
│         AppCredentialProvider returns AgentforceAuthCredentials.Guest(url = salesforceDomain)
│         (or PassThroughAuth if AuthorizationMethod.PASSTHROUGH is configured on the org)
│         Prerequisites: esDeveloperName, organizationId, serviceApiURL, salesforceDomain.
│         Setup: https://help.salesforce.com/s/articleView?id=service.miaw_deployment_mobile.htm&type=5
│
└── No (employees / signed-in users)
     │
     └── How is the user authenticated?
         │
         ├── Salesforce Mobile SDK (SalesforceSDKManager / UserAccountManager)
         │   → AgentforceMode.FullConfig
         │   → AgentforceAuthCredentials.OAuth(authToken, orgId, userId)
         │
         ├── Backend that issues a Salesforce-signed JWT
         │   → AgentforceMode.FullConfig
         │   → AgentforceAuthCredentials.OrgJWT(orgJWT)
         │
         └── Other / public Agent API behind Experience Cloud
             → AgentforceMode.FullConfig (or .ServiceAgent if applicable)
             → AgentforceAuthCredentials.Guest(url = ...)
             → Only suggest this when the user explicitly describes
               "public Agent API + Experience Cloud site + no JWT".
```

## When `Guest(url)` is correct

For Service Agents, **always** — the SDK requires a credential provider, and the canonical pattern returns `Guest(salesforceDomain)` for unauthenticated public agents.

For non-service-agent flows, only when **all** of these hold:

- The agent is publicly accessible.
- The consumer is going through the **Agent API** (not MIAW / service agent).
- The org exposes the agent via an **Experience Cloud site**.
- The consumer cannot or does not want to issue a JWT.

If any of these are unclear in a non-service flow, default to `ServiceAgent` (Branch B in `SKILL.md`).

## When `OrgJWT` is correct

Only when **all** of these hold:

- Employee/internal users (signed-in).
- The consumer has a backend (or another mechanism) that mints a Salesforce-signed JWT for the user.
- They do **not** want to manage OAuth tokens directly.

JWT lifetime is the consumer's responsibility. `getAuthCredentials()` should fetch the *current* JWT each time — don't cache a fresh-at-init value, it'll expire.

## When `PassThroughAuth` is correct

Only when **all** of these hold:

- Service Agent flow (`AgentforceMode.ServiceAgent`).
- The MIAW deployment is configured with `AuthorizationMethod.PASSTHROUGH`.
- The consumer's backend mints a MIAW JWT.

In this case, also implement `fetchMIAWJWTForPassthrough(pathOrUrl, serviceApiUrl, esDeveloperName)` on `AgentforceAuthCredentialProvider` — the SDK calls it on demand and expects an `AgentforceAuthCredentials.PassThroughAuth?` back.

## Prerequisites checklist

Before scaffolding, confirm the user has:

| Branch | Prerequisites |
|---|---|
| Service Agent | MIAW mobile deployment configured; `esDeveloperName`, `organizationId`, `serviceApiURL`, `salesforceDomain` |
| Employee + OAuth | Salesforce Mobile SDK already integrated **or** a token source class to wrap; `salesforceDomain`; `agentId` |
| Employee + OrgJWT | A way to fetch a current JWT; `salesforceDomain`; `agentId` |
| Guest (non-service) | Agent API endpoint URL; `salesforceDomain`; `agentId` |

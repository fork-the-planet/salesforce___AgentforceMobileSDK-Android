// OkHttp-backed Network implementation.
//
// If your app already configures interceptors (auth refresh, telemetry,
// certificate pinning) on a shared OkHttpClient, pass that instance in
// rather than building a fresh one here.

package {{PACKAGE}}.agentforce

import com.salesforce.android.mobile.interfaces.network.Network
import com.salesforce.android.mobile.interfaces.network.NetworkRequest
import com.salesforce.android.mobile.interfaces.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class AppNetwork(
    private val okHttpClient: OkHttpClient = OkHttpClient()
) : Network {

    override suspend fun perform(request: NetworkRequest): NetworkResponse =
        withContext(Dispatchers.IO) {
            val httpRequest = Request.Builder().apply {
                url(request.path)
                request.headers.forEach { (key, value) -> addHeader(key, value) }

                when (request.method) {
                    NetworkRequest.Method.GET -> get()
                    NetworkRequest.Method.DELETE -> delete()
                    NetworkRequest.Method.POST -> {
                        val mediaType = request.contentType?.toMediaType()
                            ?: "application/json".toMediaType()
                        post(request.body?.toRequestBody(mediaType))
                    }
                    NetworkRequest.Method.PUT -> {
                        val mediaType = request.contentType?.toMediaType()
                            ?: "application/json".toMediaType()
                        put(request.body?.toRequestBody(mediaType))
                    }
                    else -> throw IllegalArgumentException(
                        "Unsupported HTTP method: ${request.method}"
                    )
                }
            }.build()

            okHttpClient.newCall(httpRequest).execute().use { response ->
                NetworkResponse(
                    body = response.body?.string()?.toByteArray(),
                    statusCode = response.code,
                    headers = response.headers.toMap()
                )
            }
        }

    private fun okhttp3.Headers.toMap(): Map<String, String> =
        names().associateWith { name -> get(name).orEmpty() }
}

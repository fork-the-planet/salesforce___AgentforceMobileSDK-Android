// android.util.Log-backed Logger.
//
// Visible in Logcat with filter `tag:AgentforceSDK`, or in a terminal
// via `adb logcat -s AgentforceSDK`.
//
// If you already use Timber/SLF4J/etc., wrap your existing logger here
// instead of introducing a duplicate.

package {{PACKAGE}}.agentforce

import android.util.Log
import com.salesforce.android.mobile.interfaces.logging.Logger

class AppLogger : Logger {

    override fun e(message: String) {
        Log.e(TAG, message)
    }

    override fun e(message: String, exception: Throwable) {
        Log.e(TAG, message, exception)
    }

    override fun i(message: String) {
        Log.i(TAG, message)
    }

    override fun i(message: String, exception: Throwable) {
        Log.i(TAG, message, exception)
    }

    override fun w(message: String) {
        Log.w(TAG, message)
    }

    override fun w(message: String, exception: Throwable) {
        Log.w(TAG, message, exception)
    }

    companion object {
        private const val TAG = "AgentforceSDK"
    }
}

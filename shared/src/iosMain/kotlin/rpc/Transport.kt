@file:Suppress("UNUSED_PARAMETER")

package rpc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import platform.Foundation.*
import platform.darwin.NSObject
import platform.darwin.dispatch_get_current_queue
import platform.darwin.dispatch_sync
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.native.concurrent.freeze

class Transport {
    internal suspend fun <T> get(
        url: String,
        deserializationStrategy: KSerializer<T>,
        vararg args: Pair<String, Any>
    ): T {
        return Json.parse(deserializationStrategy, fetch(url, *args))
    }

    internal suspend fun <T> getList(
        url: String,
        deserializationStrategy: KSerializer<T>,
        vararg args: Pair<String, Any>
    ): List<T> {
        return Json.parse(deserializationStrategy.list, fetch(url, *args))
    }

    private suspend fun fetch(method: String, vararg args: Pair<String, Any>): String {
        var url = "http://localhost:8080/api/$method"
        if (args.isNotEmpty()) {
            url += "?"
            url += args.joinToString("&", transform = { "${it.first}=${urlEncode(it.second.toString())}" })
        }

        NSLog("fetch $method $url")
        return doRequest(
            HttpMethod.Get, url, headers = mapOf(
                "Accept" to "application/json",
                "Content-Type" to "application/json"
            )
        )
    }
}

@SharedImmutable
private val configuration = NSURLSessionConfiguration.defaultSessionConfiguration()

private enum class HttpMethod { Get, Post }

private suspend fun doRequest(
    method: HttpMethod,
    url: String,
    formParameters: Map<String, String?> = emptyMap(),
    headers: Map<String, String?> = emptyMap()
): String = suspendCoroutine { callback ->
    val queue = dispatch_get_current_queue().freeze()
    val delegate = object : NSObject(), NSURLSessionDataDelegateProtocol {
        val receivedData = NSMutableData()

        override fun URLSession(session: NSURLSession, dataTask: NSURLSessionDataTask, didReceiveData: NSData) {
            initRuntimeIfNeeded()
            receivedData.appendData(didReceiveData)
        }

        override fun URLSession(session: NSURLSession, didBecomeInvalidWithError: NSError?) {
            callback.resumeWithException(error("HttpRequest did become invalid"))
        }

        override fun URLSession(session: NSURLSession, task: NSURLSessionTask, didCompleteWithError: NSError?) {
            if (didCompleteWithError == null) {
                val data = receivedData.string().freeze()
                NSLog("current thread before sync %@",  NSThread.currentThread)
                dispatch_sync(queue) {
                    NSLog("current thread after sync %@",  NSThread.currentThread)
                    callback.resume(data ?: error("Cannot convert NSData to string"))
                }
            } else {
                callback.resumeWithException(error(didCompleteWithError.localizedDescription))
            }
        }
    }

    NSLog("current queue %@", queue)
    NSLog("current thread %@",  NSThread.currentThread)
    val session = NSURLSession.sessionWithConfiguration(configuration, delegate, null)
    val request =
        NSMutableURLRequest.requestWithURL(NSURL.URLWithString(url)!!, NSURLRequestUseProtocolCachePolicy, 15.0)

    when (method) {
        HttpMethod.Post -> {
            request.addValue("application/x-www-form-urlencoded", "Content-Type")
            request.setHTTPMethod("POST")
            TODO("POST is not supported")
        }
        HttpMethod.Get -> request.setHTTPMethod("GET")
    }
    headers.filterNot { it.value == null }.forEach {
        request.addValue(it.value!!, it.key)
    }
    session.dataTaskWithRequest(request).resume()
}

fun urlEncode(value: String): String {
    return try {
        (value as? NSString)?.stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.alphanumericCharacterSet)!!
    } catch (e: Exception) {
        NSLog("urlEncode failed")
        value
    }
}

fun NSData.string(): String? {
    return NSString.create(this, NSUTF8StringEncoding) as String?
}

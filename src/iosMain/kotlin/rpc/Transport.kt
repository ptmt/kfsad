package rpc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import platform.Foundation.*
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Transport(private val coroutineContext: CoroutineContext) {
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
        var url = "/api/$method"
        if (args.isNotEmpty()) {
            url += "?"
            url += args.joinToString("&", transform = { "${it.first}=${urlEncode(it.second.toString())}" })
        }


        return doRequest(
            HttpMethod.Get, url, headers = mapOf(
                "Accept" to "application/json",
                "Content-Type" to "application/json"
            )
        )
    }
}

private val configuration = NSURLSessionConfiguration.defaultSessionConfiguration()

private enum class HttpMethod { Get, Post }

private suspend fun doRequest(
    method: HttpMethod,
    url: String,
    formParameters: Map<String, String?> = emptyMap(),
    headers: Map<String, String?> = emptyMap()
): String = suspendCoroutine { callback ->

    val delegate = object : NSObject(), NSURLSessionDataDelegateProtocol {
        val receivedData = NSMutableData()
        @Suppress("UNUSED_PARAMETER")
        override fun URLSession(session: NSURLSession, dataTask: NSURLSessionDataTask, didReceiveData: NSData) {
            receivedData.appendData(didReceiveData)
        }

        override fun URLSession(session: NSURLSession, didBecomeInvalidWithError: NSError?) {
            callback.resumeWithException(error("HttpRequest did become invalid"))
        }

        @Suppress("UNUSED_PARAMETER")
        override fun URLSession(session: NSURLSession, task: NSURLSessionTask, didCompleteWithError: NSError?) {
            if (didCompleteWithError == null) {
                val httpResponse = (task.response()!! as NSHTTPURLResponse)
                callback.resume(receivedData.string()!!)
            } else {
                callback.resumeWithException(error(didCompleteWithError.localizedDescription))
            }
        }
    }
    val queue = NSOperationQueue.mainQueue
    val session = NSURLSession.sessionWithConfiguration(configuration, delegate, queue)
    val request =
        NSMutableURLRequest.requestWithURL(NSURL.URLWithString(url)!!, NSURLRequestUseProtocolCachePolicy, 30.0)

    when (method) {
        HttpMethod.Post -> {
            request.addValue("application/x-www-form-urlencoded", "Content-Type")
            request.setHTTPMethod("POST")
//                val formString = formParameters.entries.joinToString(separator = "&") {
//                    "${it.key.encodeURIComponent()}=${it.value?.encodeURIComponent()}"
//                }
//                request.setHTTPBody(formString.nsData())
        }
        HttpMethod.Get -> request.setHTTPMethod("GET")
    }
    headers.filterNot { it.value == null }.forEach {
        request.addValue(it.value!!, it.key)
    }
    session.dataTaskWithRequest(request).resume()
}

fun urlEncode(value: String): String {
    try {
        return (value as NSString).stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.alphanumericCharacterSet)!!
    } catch (e: Exception) {
        return value
    }
}

fun NSData.string(): String? {
    return NSString.create(this, NSUTF8StringEncoding) as String?
}

package services

import model.PostWithComments
import rpc.Transport
import kotlinx.coroutines.*
import platform.Foundation.NSLog
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze

actual class PostWithCommentsService {
    private val transport = Transport().freeze()
    private val backgroundWorker = Worker.start()

    actual suspend fun getPostsWithComments(): List<PostWithComments> {
        return transport.getList("getPostsWithComments", PostWithComments.serializer())
    }

    actual suspend fun getPostWithComments(postId: String): PostWithComments {
        return transport.get("getPostWithComments", PostWithComments.serializer(), "postId" to postId)
    }

    fun getPostsWithComments(completion: (List<PostWithComments>) -> Unit) {
        val future = backgroundWorker.execute(TransferMode.SAFE, { transport to PostWithComments.serializer() }) { (t, serializer) ->
            val posts = runBlocking {
                t.getList("getPostsWithComments", serializer)
            }
            NSLog("getPostsWithComments posts ${posts.size}")
            posts.freeze()
        }
        future.consume {
            completion(it)
        }
    }

}
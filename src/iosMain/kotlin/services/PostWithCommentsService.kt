package services

import model.PostWithComments
import rpc.Transport
import kotlinx.coroutines.*
import platform.Foundation.NSLog

actual class PostWithCommentsService {
    private val transport = Transport()

    actual suspend fun getPostsWithComments(): List<PostWithComments> {
        return transport.getList("getPostsWithComments", PostWithComments.serializer())
    }

    actual suspend fun getPostWithComments(postId: String): PostWithComments {
        return transport.get("getPostWithComments", PostWithComments.serializer(), "postId" to postId)
    }

    fun getPostsWithComments(completion: (List<PostWithComments>) -> Unit) {
        NSLog("getPostsWithComments kotlin")
        GlobalScope.launch {
            NSLog("getPostsWithComments kotlin inside coroutine")
            val posts = transport.getList("getPostsWithComments", PostWithComments.serializer())
            NSLog("getPostsWithComments posts ${posts.size}")
            completion(posts)
        }
    }

}
package services

import model.Post
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class PostService() {
    private val transport = Transport()

    actual suspend fun getPost(id: String): Post {
        return transport.get("getPost", Post.serializer(), "id" to id)
    }

    actual suspend fun getPosts(): List<Post> {
        return transport.getList("getPosts", Post.serializer())
    }

}
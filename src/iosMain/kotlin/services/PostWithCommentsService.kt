package services

import model.PostWithComments
import rpc.Transport

actual class PostWithCommentsService {
    private val transport = Transport()

    actual suspend fun getPostsWithComments(): List<PostWithComments> {
        return transport.getList("getPostsWithComments", PostWithComments.serializer())
    }

    actual suspend fun getPostWithComments(postId: String): PostWithComments {
        return transport.get("getPostWithComments", PostWithComments.serializer(), "postId" to postId)
    }

}
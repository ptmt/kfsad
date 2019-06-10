package network

import kotlin.coroutines.CoroutineContext

actual class CommentClient actual constructor(coroutineContext: CoroutineContext) {
    actual suspend fun getComments(postId: String, count: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
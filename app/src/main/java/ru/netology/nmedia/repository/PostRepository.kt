package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long, likedByMe: Boolean) : Post
    fun save(post: Post)
    fun removeById(id: Long)

    fun getAllAsync(callback: GetAllCallback)
    interface GetAllCallback {
        fun onSuccess(posts:List<Post>)
        fun onError()
    }

    fun saveAsync(callback: SaveCallback, post: Post)
    interface SaveCallback {
        fun onSuccess()
        fun onError()
    }
}


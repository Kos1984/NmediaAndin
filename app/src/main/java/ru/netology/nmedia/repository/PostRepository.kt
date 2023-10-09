package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
   // fun getAll(): List<Post>
    //fun likeById(id: Long, likedByMe: Boolean) : Post
    //fun save(post: Post)
    fun removeById(id: Long)

    fun saveAsync(callback: RepositoryCallback<Unit>, post: Post)
    fun getAllAsync(callback: RepositoryCallback<List<Post>>)
    fun likeByIdAsync(callback: RepositoryCallback<Post>, id: Long, likedByMe: Boolean)
    fun removeByIdAsync(callback: RepositoryCallback<Unit>, id: Long)



    interface RepositoryCallback<T> {
        fun onSuccess(value: T)
        fun onError()
    }
}


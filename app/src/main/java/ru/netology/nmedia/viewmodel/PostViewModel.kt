package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
            // Начинаем загрузку
            _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.RepositoryCallback<List<Post>>{
            override fun onSuccess(value: List<Post>) {
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))
            }

            override fun onError() {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun save() {
        edited.value?.let {
            repository.saveAsync(object : PostRepository.RepositoryCallback<Unit>{
                override fun onSuccess(value: Unit) {
                    loadPosts()
                }
                override fun onError() {
                }
            }, it)
            _postCreated.postValue(Unit)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long, likedByMe: Boolean) {
         repository.likeByIdAsync(object : PostRepository.RepositoryCallback<Post>{
            override fun onSuccess(value: Post) {
                _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map {
                        if (it.id == value.id){
                            it.copy(likedByMe = value.likedByMe, likes = value.likes)
                        }else it
                    }
                )
                )
            }

            override fun onError() {
                TODO("Not yet implemented")
            }

        }, id, likedByMe)

    }

    fun removeById(id: Long) {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            try {
                repository.removeByIdAsync(object : PostRepository.RepositoryCallback<Unit>{
                    override fun onSuccess(value: Unit) {
                        _data.postValue(
                            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                                .filter { it.id != id }
                            )
                        )
                    }

                    override fun onError() {
                        TODO("Not yet implemented")
                    }

                }, id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }

    }
}

package ru.netology.nmedia.repository

import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class PostRepositoryImpl: PostRepository {

    override fun getAllAsync(callback: PostRepository.RepositoryCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>>{
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(response.isSuccessful){
                    callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                }else{
                    callback.onError(RuntimeException("error code ${response.code()} with ${response.message()}"))
                }
            }
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t))
            }


        })
    }

    override fun likeByIdAsync(callback: PostRepository.RepositoryCallback<Post>, id: Long, likedByMe: Boolean) {
         if (!likedByMe){
             PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post>{
                 override fun onResponse(call: Call<Post>, response: Response<Post>) {
                     if(response.isSuccessful){
                         callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                     }else{
                         callback.onError(RuntimeException("error code ${response.code()} with ${response.message()}"))
                     }
                 }
                 override fun onFailure(call: Call<Post>, t: Throwable) {
                     callback.onError(Exception(t))
                 }
             })
        }else {
             PostsApi.retrofitService.deleteLikeById(id).enqueue(object : Callback<Post>{
                 override fun onResponse(call: Call<Post>, response: Response<Post>) {
                     if(response.isSuccessful){
                         callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                     }else{
                         callback.onError(RuntimeException("error code ${response.code()} with ${response.message()}"))
                     }
                 }

                 override fun onFailure(call: Call<Post>, t: Throwable) {
                     callback.onError(Exception(t))
                 }

             })
        }
    }

    override fun saveAsync(callback: PostRepository.RepositoryCallback<Unit>, post: Post) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post>{
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(response.isSuccessful){
                        callback.onSuccess(Unit)
                    }else{
                        callback.onError(RuntimeException("error code ${response.code()} with ${response.message()}"))
                    }
                }
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

        })
    }

    override fun removeByIdAsync(callback: PostRepository.RepositoryCallback<Unit>, id: Long) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if(response.isSuccessful){
                        callback.onSuccess(Unit)
                    }else{
                        callback.onError(RuntimeException("error code ${response.code()} with ${response.message()}"))
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t))
                }


        })
    }
}

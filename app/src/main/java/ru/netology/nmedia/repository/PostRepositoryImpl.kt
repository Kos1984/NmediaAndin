package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit
//import retrofit2.Callback



class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    //private val typeToken = object : TypeToken<List<Post>>() {}
    private var request1: Request? = null

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }


    override fun getAllAsync(callback: PostRepository.RepositoryCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : retrofit2.Callback<List<Post>>{
            override fun onResponse(call: retrofit2.Call<List<Post>>, response: retrofit2.Response<List<Post>>) {
                if(response.isSuccessful){
                    callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                }else{
                    callback.onError(RuntimeException("error code ${response.code()} with ${response.message()}"))
                }
            }
            override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                callback.onError(Exception(t))
            }


        })
    }

    override fun likeByIdAsync(callback: PostRepository.RepositoryCallback<Post>, id: Long, likedByMe: Boolean) {
        request1 = if (!likedByMe){
            Request.Builder()
                .post(EMPTY_REQUEST)
                .url("${BASE_URL}/api/slow/posts/$id/likes")
                .build()
        }else {
            Request.Builder()
                .delete(EMPTY_REQUEST)
                .url("${BASE_URL}/api/slow/posts/$id/likes")
                .build()
        }
        client.newCall(request1!!)
            .enqueue(object : Callback{
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    callback.onSuccess( gson.fromJson(body, Post ::class.java))
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun saveAsync(callback: PostRepository.RepositoryCallback<Unit>, post: Post) {
        PostsApi.retrofitService.save(post).enqueue(object : retrofit2.Callback<Post>{
                override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
                    if(response.isSuccessful){
                        callback.onSuccess(Unit)
                    }else{
                        callback.onError(RuntimeException("error code ${response.code()} with ${response.message()}"))
                    }
                }
                override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }

        })
    }

    override fun removeByIdAsync(callback: PostRepository.RepositoryCallback<Unit>, id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        client.newCall(request)
            .enqueue(object : Callback{
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Unit)
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

            })
    }
}

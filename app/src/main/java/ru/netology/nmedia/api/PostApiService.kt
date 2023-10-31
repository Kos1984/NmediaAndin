package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post

// Вставляем URL настроенный в build.gradle
private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"


interface PostApiService {
    @GET("posts")
    fun getAll(): Call<List<Post>>

    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Long) : Call<Unit>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    fun deleteLikeById(@Path("id") id: Long): Call<Post>

}
// создаем и настраиваем логгер
val logger = HttpLoggingInterceptor().apply {
    if(BuildConfig.DEBUG) {
        // глубина логгирования
        level = HttpLoggingInterceptor.Level.BODY
    }
}
    // создаем коиент и передаем туда логгер
val client = OkHttpClient.Builder().addInterceptor(logger).build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client) // передали клиент логгирования и переходим в настройку build.gradle
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object PostsApi{
    val retrofitService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}
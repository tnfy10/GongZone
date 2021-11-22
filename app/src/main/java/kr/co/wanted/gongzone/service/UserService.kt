package kr.co.wanted.gongzone.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.co.wanted.gongzone.BuildConfig
import kr.co.wanted.gongzone.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {
    @FormUrlEncoded
    @POST("users.php")
    fun getUser(@Field("userId") userId: String,
                @Field("pwd") pwd: String) : Call<User>

    companion object {
        private val gson: Gson = GsonBuilder().setLenient().create()
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(createOkHttpClient())
            .build()

        private fun createOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            return builder.build()
        }

        fun create(): UserService = retrofit.create(UserService::class.java)
    }
}
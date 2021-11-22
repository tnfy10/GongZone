package kr.co.wanted.gongzone.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.co.wanted.gongzone.model.geocode.Geocode
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverAPI {
    @GET("map-geocode/v2/geocode")
    fun getGeocode(@Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
                   @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
                   @Header("Accept") accept: String,
                   @Query("query") address: String) : Call<Geocode>

    companion object {
        const val accept = "application/json"

        private const val URL = "https://naveropenapi.apigw.ntruss.com/"
        private val gson: Gson = GsonBuilder().setLenient().create()
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL)
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

        fun create(): NaverAPI = retrofit.create(NaverAPI::class.java)
    }
}
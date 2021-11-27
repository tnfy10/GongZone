package kr.co.wanted.gongzone.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.co.wanted.gongzone.BuildConfig
import kr.co.wanted.gongzone.model.pin.Pin
import kr.co.wanted.gongzone.model.seat.Seat
import kr.co.wanted.gongzone.model.space.Space
import kr.co.wanted.gongzone.model.voucher.Voucher
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SpaceService {
    @GET("/space/space.php")
    fun getSpace(@Query("spaceNum") spaceNum: String) : Call<Space>

    @GET("/space/pins.php")
    fun getSpaceList(): Call<Pin>

    @GET("/space/seats.php")
    fun getSeatsInfo(@Query("spaceNum") spaceNum: String) : Call<Seat>

    @GET("/voucher/voucher.php")
    fun getUserVoucherList(@Query("userNum") userNum: String) : Call<Voucher>

    @FormUrlEncoded
    @POST("voucher/voucher.php")
    fun paymentVoucher(@Field("userNum") userNum: String,
                       @Field("type") type: String,
                       @Field("availableTime") availableTime: String,
                       @Field("day") day: String) : Call<ResponseBody>

    @FormUrlEncoded
    @PUT("/space/seats.php")
    fun enterRoom(@Field("seatNum") seatNum: String,
                  @Field("userNum") userNum: String,
                  @Field("voucherNum") voucherNum: String,
                  @Field("spaceNum") spaceNum: String,
                  @Field("type") type: String) : Call<ResponseBody>

    @FormUrlEncoded
    @PUT("voucher/voucher.php")
    fun exitRoom(@Field("seatNum") seatNum: String,
                 @Field("userNum") userNum: String,
                 @Field("voucherNum") voucherNum: String,
                 @Field("availableTime") availableTime: String) : Call<ResponseBody>

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

        fun create(): SpaceService = retrofit.create(SpaceService::class.java)
    }
}
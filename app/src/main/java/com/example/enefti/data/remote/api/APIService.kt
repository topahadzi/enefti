package com.example.enefti.data.remote.api

import CollectionsList
import NFTSList
import TopCollectionsList
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.DetailCollections
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
        @GET("/v1/exchanges/collections?page_size=25")    //End Url
        fun getCollections(@Header("X-API-KEY") apiKey: String,
                @Query("chain") chain:String,
                @Query("exchange") exchange:String,
        ): Call<CollectionsList>

        @GET("/v1/exchanges/collectionsranking")    //End Url
        fun getTopCollections(@Header("X-API-KEY") apiKey: String,
                           @Query("chain") chain:String,
                           @Query("exchange") exchange:String,
                              @Query("ranking") ranking:String,
                              @Query("page_size") page_size:String,
        ): Call<TopCollectionsList>
        @GET("/v1/exchanges/collections/key/{key}")    //End Url
        fun getDetailsCollection(@Header("X-API-KEY") apiKey: String,
                                 @Path("key") key:String,
                                 @Query("chain") chain:String,
                                 @Query("exchange") exchange:String,


        ): Call<DetailCollections>
        @GET("/v1/nfts/contract/{contract_address}")    //End Url
        fun getNFTByCollections(@Header("X-API-KEY") apiKey: String,
                               @Path("contract_address") contract_address:String,
                              @Query("chain") chain:String,
        ): Call<NFTSList>
        @GET("/v1/exchanges/collections/search?page_size=25")    //End Url
        fun getSearchCollections(@Header("X-API-KEY") apiKey: String,
                           @Query("chain") chain:String,
                           @Query("exchange") exchange:String,
                                 @Query("filter") filter:String,
                                 @Query("q") q:String,
        ): Call<CollectionsList>
        companion object {

                var retrofitService: APIService? = null
                val READ_TIMEOUT_SECONDS = 30L
                val CONNECT_TIMEOUT_SECONDS = 30L

                // Create a custom OkHttpClient with timeout settings
                val httpClient = OkHttpClient.Builder()
                        .readTimeout(READ_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                        .connectTimeout(CONNECT_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                        .build()
                //Create the Retrofit service instance using the retrofit.
                fun getInstance(): APIService {

                        if (retrofitService == null) {
                                val retrofit = Retrofit.Builder()
                                        .baseUrl("https://api.blockspan.com")
                                        .client(httpClient)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                retrofitService = retrofit.create(APIService::class.java)
                        }
                        return retrofitService!!
                }
        }
}
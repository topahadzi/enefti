package com.example.enefti.data.repository

import CollectionsList
import NFTSList
import TopCollectionsList
import android.util.Log
import com.example.enefti.data.remote.api.APIService
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.DetailCollections
import retrofit2.Call

class OpenSeaRepository constructor(private val apiService: APIService) {

     fun getCollections(apiKey: String, chain: String, exchange: String): Call<CollectionsList> {
          return apiService.getCollections(apiKey, chain, exchange)
     }
     fun getTopCollections(apiKey: String, chain: String, exchange: String, ranking: String, page_size: String): Call<TopCollectionsList> {
          return apiService.getTopCollections(apiKey, chain, exchange, ranking, page_size)
     }
     fun getDetailsCollection(apiKey: String, chain: String, exchange: String, key: String): Call<DetailCollections> {
          Log.d("masuk repo", "detail repo ${apiService.getDetailsCollection(apiKey, chain, exchange, key)}")
          return apiService.getDetailsCollection(apiKey, chain, exchange, key)
     }
     fun getNFTByCollections(apiKey: String, contract_address: String, chain: String): Call<NFTSList> {
          Log.d("masuk repo", "${apiService.getNFTByCollections(apiKey, contract_address, chain)}")
          return apiService.getNFTByCollections(apiKey, contract_address, chain)
     }
     fun getSearchCollections(apiKey: String, chain: String, exchange: String, filter: String, q: String): Call<CollectionsList> {
          return apiService.getSearchCollections(apiKey, chain, exchange, filter, q)
     }
}
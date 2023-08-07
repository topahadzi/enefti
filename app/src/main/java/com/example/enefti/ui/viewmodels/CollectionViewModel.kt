package com.example.enefti.ui.viewmodels

import CollectionsList
import NFTSList
import TopCollectionsList
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.DetailCollections
import com.example.enefti.data.remote.model.NFTS
import com.example.enefti.data.remote.model.TopCollections
import com.example.enefti.data.repository.OpenSeaRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class CollectionViewModel(private val repository: OpenSeaRepository) : ViewModel() {

    val collectionsList = MutableLiveData<List<Collections>>()
    val TopCollectionsList = MutableLiveData<List<TopCollections>>()
    val SearchCollectionsList = MutableLiveData<List<Collections>>()
    val NFTSList = MutableLiveData<List<NFTS>>()
    val detailCollectionsLiveData: MutableLiveData<DetailCollections> = MutableLiveData()
    val errorMessage = MutableLiveData<String>()

    fun getCollections() {
        val response = repository.getCollections("1VxRE2cjXnfvnCOtGMGW1rQGJiagBxGQ","eth-main","opensea")
        response.enqueue(object : Callback<CollectionsList> {
            override fun onResponse(call: Call<CollectionsList>, response: Response<CollectionsList>) {
                collectionsList.postValue(response.body()?.mList)
            }

            override fun onFailure(call: Call<CollectionsList>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    fun getTopCollections(rankings: String, page_size: String) {
        val response = repository.getTopCollections("1VxRE2cjXnfvnCOtGMGW1rQGJiagBxGQ","eth-main","opensea", rankings, page_size)
        response.enqueue(object : Callback<TopCollectionsList> {
            override fun onResponse(call: Call<TopCollectionsList>, response: Response<TopCollectionsList>) {
                TopCollectionsList.postValue(response.body()?.mList)
            }

            override fun onFailure(call: Call<TopCollectionsList>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    fun getNFTByCollections(contract_address: String) {
        val response = repository.getNFTByCollections("1VxRE2cjXnfvnCOtGMGW1rQGJiagBxGQ",contract_address,"eth-main")
        response.enqueue(object : Callback<NFTSList> {
            override fun onResponse(call: Call<NFTSList>, response: Response<NFTSList>) {
                Log.d("test", "${response.body()}")
                NFTSList.postValue(response.body()?.mList)
            }

            override fun onFailure(call: Call<NFTSList>, t: Throwable) {
                errorMessage.postValue(t.toString())
            }
        })
    }
    fun getDetailsCollection(key: String) {
        Log.d("test", "key = $key")
        val response = repository.getDetailsCollection("1VxRE2cjXnfvnCOtGMGW1rQGJiagBxGQ",key,"eth-main","opensea")
        response.enqueue(object : Callback<DetailCollections> {
            override fun onResponse(call: Call<DetailCollections>, response: Response<DetailCollections>) {
                Log.d("test", "response ${response}")
                detailCollectionsLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<DetailCollections>, t: Throwable) {
                errorMessage.postValue(t.toString())
            }
        })
    }
    fun searchCollections(q: String) {
        val response = repository.getSearchCollections("1VxRE2cjXnfvnCOtGMGW1rQGJiagBxGQ","eth-main","opensea", "name", q)
        response.enqueue(object : Callback<CollectionsList> {
            override fun onResponse(call: Call<CollectionsList>, response: Response<CollectionsList>) {
                SearchCollectionsList.postValue(response.body()?.mList)
            }

            override fun onFailure(call: Call<CollectionsList>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}
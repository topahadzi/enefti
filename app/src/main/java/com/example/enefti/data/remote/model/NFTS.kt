package com.example.enefti.data.remote.model

import com.google.gson.annotations.SerializedName

data class NFTS(
    @SerializedName("id") val id: String,
    @SerializedName("token_name") val token_name: String,
    @SerializedName("token_description") val token_description: String,
    @SerializedName("uri") val uri: String,
//    @SerializedName("contract_address") val contract_address: String,
    @SerializedName("metadata") val metadata : Metadatas ?= null,
    @SerializedName("token_type") val token_type : String,
    @SerializedName("recent_price") val recent_price: RecentPrice,
){
    data class RecentPrice(
        @SerializedName("price") val price: String,
    )
}
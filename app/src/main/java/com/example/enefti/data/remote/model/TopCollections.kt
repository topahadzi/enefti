package com.example.enefti.data.remote.model

import com.google.gson.annotations.SerializedName

data class TopCollections(
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("key") val key: String,
    @SerializedName("banner_image_url") val banner_image_url: String,
    @SerializedName("description") val description: String,
    @SerializedName("contracts") val contract : List<contracts>,
    @SerializedName("total_volume") val total_volume: String,
    @SerializedName("total_sales") val total_sales: String,
){
    data class contracts(
        @SerializedName("contract_address") val contract_address : String
    )
}
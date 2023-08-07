package com.example.enefti.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enefti.R
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.NFTS
import com.example.enefti.databinding.ItemCollectionBinding
import com.example.enefti.databinding.ItemNftBinding
import com.google.gson.Gson

class NFTAdapter : RecyclerView.Adapter<NFTSViewHolder>() {

    var nft = mutableListOf<NFTS>()
//    var collection: Collections = Collections()
    fun setNFTList(collections: List<NFTS>) {
        this.nft = collections.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NFTSViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemNftBinding.inflate(inflater, parent, false)
        return NFTSViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NFTSViewHolder, position: Int) {
        val nft = nft[position]
        val img = loadImageFromIPFS(nft.metadata?.image.toString())
        if (nft.token_name == null) {
            holder.binding.nftTitle.text = "#"+nft.id
        } else {
            holder.binding.nftTitle.text = nft.token_name
        }
        if(nft.recent_price == null || nft.recent_price.price == null ){
            holder.binding.nftFloorValue.text = "-"
        } else {
            Log.d("test","test price ${nft.recent_price.price}")
            if(nft.recent_price.price.contains(".")){
                holder.binding.nftFloorValue.text = formatNumberWithTwoDigitsAfterComma(nft.recent_price.price.toDouble())+" ETH"
            } else {
                holder.binding.nftFloorValue.text = nft.recent_price.price+" ETH"
            }

        }


        Glide.with(holder.itemView.context).load(img).placeholder(R.drawable.collection_example)
            .into(holder.binding.nftImg)

        holder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(Gson().toJson(nft))
        }

    }

    override fun getItemCount(): Int {
        return nft.size
    }
    private var onItemClickListener: ((String?) -> Unit)? = null

    fun setOnItemClickListener(listener: (String?) -> Unit) {
            onItemClickListener = listener
    }
    fun loadImageFromIPFS(ipfsCidUrl: String): String? {
        var imageUrl = ipfsCidUrl
        if (!ipfsCidUrl.contains("https")) {
            val ipfsHash = ipfsCidUrl.substringAfter("ipfs://") // Extract the CID from the URL
            imageUrl = "https://ipfs.io/ipfs/$ipfsHash"
        }
        return imageUrl
    }
    fun formatNumberWithTwoDigitsAfterComma(value: Double): String {
        return String.format("%.2f", value)
    }

}

class NFTSViewHolder(val binding: ItemNftBinding) : RecyclerView.ViewHolder(binding.root) {}

package com.example.enefti.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enefti.R
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.databinding.ItemCollectionBinding
import com.example.enefti.databinding.ItemHighlightBinding
import com.google.gson.Gson
import kotlin.math.pow

class CollectionAdapter : RecyclerView.Adapter<CollectionsViewHolder>() {

    var collections = mutableListOf<Collections>()

    fun setCollectionsList(collections: List<Collections>) {
        this.collections = collections.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemCollectionBinding.inflate(inflater, parent, false)
        return CollectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        val collections = collections[position]
        if (collections.stats.total_volume.contains(".")) {
            val totalVolume = collections.stats.total_volume.substringBefore(".").toInt()
            holder.binding.collectionTotalvolumeValue.text = totalVolume.toShortNumberString()
        }else{
            val totalVolume = collections.stats.total_volume.toInt()
            holder.binding.collectionTotalvolumeValue.text = totalVolume.toShortNumberString()
        }

        holder.binding.collectionFloorValue.text = collections.stats.floor_price
        holder.binding.collectionTitle.text = collections.name
        Glide.with(holder.itemView.context).load(collections.image_url).placeholder(R.drawable.collection_example)
            .into(holder.binding.collectionImg)

        holder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(Gson().toJson(collections))
        }


    }

    override fun getItemCount(): Int {
        return collections.size
    }
    private var onItemClickListener: ((String?) -> Unit)? = null

    fun setOnItemClickListener(listener: (String?) -> Unit) {
            onItemClickListener = listener
    }
    fun Int.toShortNumberString(): String {
        if (this < 1000) {
            // If the number is less than 1000, return it as is
            return this.toString()
        }

        val suffixes = arrayOf("", "K", "M", "B", "T") // Add more suffixes as needed
        val numDigits = (Math.log10(this.toDouble()) / 3).toInt()
        val roundedNumber = this / 10.0.pow(numDigits * 3)

        return String.format("%.1f", roundedNumber) + suffixes[numDigits]
    }

}

class CollectionsViewHolder(val binding: ItemCollectionBinding) : RecyclerView.ViewHolder(binding.root) {}

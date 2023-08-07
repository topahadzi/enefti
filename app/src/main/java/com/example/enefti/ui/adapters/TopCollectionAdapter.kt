package com.example.enefti.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enefti.R
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.TopCollections
import com.example.enefti.databinding.ItemCollectionBinding
import com.example.enefti.databinding.ItemTopCollectionBinding
import com.google.gson.Gson
import kotlin.math.pow

class TopCollectionAdapter : RecyclerView.Adapter<TopCollectionsViewHolder>() {

    var collections = mutableListOf<TopCollections>()

    fun setCollectionsList(collections: List<TopCollections>) {
        this.collections = collections.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopCollectionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemTopCollectionBinding.inflate(inflater, parent, false)
        return TopCollectionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopCollectionsViewHolder, position: Int) {
        val collections = collections[position]
        val rank_number = position + 1
        val totalVolume = collections.total_volume.substringBefore(".").toInt()
        val totalSales = collections.total_sales.toInt()
        holder.binding.collectionTitle.text = collections.name
        holder.binding.rankNumber.text = rank_number.toString()
        holder.binding.collectionTotalvolume.text = totalVolume.toShortNumberString()
        holder.binding.collectionSales.text = totalSales.toShortNumberString()
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

class TopCollectionsViewHolder(val binding: ItemTopCollectionBinding) : RecyclerView.ViewHolder(binding.root) {}

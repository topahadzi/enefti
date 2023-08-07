package com.example.enefti.ui.fragments
//Nama: Mustapha Hadzi
//Kelas: IF-10
//NIM: 10120901
//Tanggal Pengerjaan: 11 Juni 2023
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enefti.R
import com.example.enefti.data.remote.api.APIService
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.DetailCollections
import com.example.enefti.data.repository.OpenSeaRepository
import com.example.enefti.databinding.FragmentCollectionBinding
import com.example.enefti.databinding.FragmentHomeBinding
import com.example.enefti.ui.adapters.CollectionAdapter
import com.example.enefti.ui.adapters.NFTAdapter
import com.example.enefti.ui.adapters.TopCollectionAdapter
import com.example.enefti.ui.viewmodels.CollectionViewModel
import com.example.enefti.ui.viewmodels.CollectionViewModelFactory
import com.google.gson.Gson
import kotlin.math.pow

class CollectionFragment : Fragment(R.layout.fragment_collection) {
    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: CollectionViewModel
    private val TAG = "CollectionActivity"
    private val retrofitService = APIService.getInstance()
    val adapter = NFTAdapter()
//    val layoutManager = LinearLayoutManager(requireContext())
//    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        binding.rvnft.adapter = adapter
//        binding.rvnft.layoutManager = layoutManager
        val data = arguments?.getString("collectionAddress")
        val collections = Gson().fromJson(data, Collections::class.java)
        viewModel =
            ViewModelProvider(
                this,
                CollectionViewModelFactory(OpenSeaRepository(retrofitService))
            ).get(
                CollectionViewModel::class.java
            )
//        binding.rvnft.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                // Check if the user has scrolled to the end of the list
//                val totalItemCount = layoutManager.itemCount
//                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//                val visibleThreshold = 25 // Adjust this value as per your requirement
//
//                if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
//                    // Load the next page of data
//                    isLoading = true
//                    viewModel.getNFTByCollections(collections.contract.firstOrNull()?.contract_address.toString())
//                }
//            }
//        })
//        viewModel.NFTSList.observe(viewLifecycleOwner, Observer {
//            Log.d(TAG, "collectionList: $it")
//            adapter.setNFTList(it)
//        })

//        val textView = view?.findViewById<TextView>(R.id.name)
//        textView?.text = data
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.NFTSList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "collectionList: $it")
            adapter.setNFTList(it)
        })

        viewModel.detailCollectionsLiveData.observe(viewLifecycleOwner, Observer { detailCollections ->
            // Update your UI or handle the data here
            Log.d("test","masuk $detailCollections")
            if (detailCollections != null) {
                val owner = detailCollections.stats.num_owners.toInt()
                val totalVolume = detailCollections.stats.total_volume.substringBefore(".").toInt()
                binding.collectionVolume.text = totalVolume.toShortNumberString()+" ETH"
                if (detailCollections.stats.floor_price != null) {
                    binding.collectionFloor.text = formatNumberWithTwoDigitsAfterComma(detailCollections.stats.floor_price.toDouble())+" ETH"
                }else {
                    binding.collectionFloor.text = "- ETH"
                }
                binding.collectionOwner.text = owner.toShortNumberString()
                if (detailCollections.instagram_username != null) {
                    binding.iconIg.setOnClickListener{
                        val url = "https://www.instagram.com/"+detailCollections.instagram_username+"/"
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }else{
                    binding.iconIg.visibility = View.GONE
                }
                if (detailCollections.external_url != null) {
                    binding.iconBrow.setOnClickListener{
                        val url = detailCollections.external_url
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }else{
                    binding.iconBrow.visibility = View.GONE
                }

                if (detailCollections.twitter_username != null) {
                    binding.iconTwitter.setOnClickListener{
                        val url = "https://twitter.com/"+detailCollections.twitter_username
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }else{
                    binding.iconTwitter.visibility = View.GONE
                }

                if (detailCollections.discord_url != null) {
                    binding.iconDiscord.setOnClickListener{
                        val url = detailCollections.discord_url
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }else{
                    binding.iconDiscord.visibility = View.GONE
                }

                // For example, update a RecyclerView or display the data in views
                // Example: myRecyclerViewAdapter.submitList(detailCollections.mList)
            } else {
                // Handle null data or show an error message if needed
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "errorMessage: $it")
        })
        val data = arguments?.getString("collectionAddress")
        val collections = Gson().fromJson(data, Collections::class.java)
//        Log.d("test", "masuk $data")
        Glide.with(binding.bannerImg.context).load(collections.banner_image_url).placeholder(R.drawable.collection_example)
                .into(binding.bannerImg)
            Glide.with(binding.collectionImg.context).load(collections.image_url).placeholder(R.drawable.collection_example)
                .into(binding.collectionImg)
        binding.collectionTitle.text = collections.name
        binding.collectionDescription.text = collections.description
        binding.arrowBack.setOnClickListener{
            findNavController().popBackStack()
        }
//        binding.rvnft.isNestedScrollingEnabled = false
        viewModel.getDetailsCollection(collections.key)
        viewModel.getNFTByCollections(collections.contract.firstOrNull()?.contract_address.toString())
        initClickAdapter()
    }
    fun initClickAdapter() {
        adapter.setOnItemClickListener {
            val data = arguments?.getString("collectionAddress")
            val bundle = Bundle()
            bundle.putString("nft", it)
            bundle.putString("collection", data)
            findNavController().navigate(R.id.navigation_nft, bundle)
        }
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
    fun formatNumberWithTwoDigitsAfterComma(value: Double): String {
        return String.format("%.2f", value)
    }
}

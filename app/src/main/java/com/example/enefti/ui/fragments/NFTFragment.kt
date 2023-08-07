package com.example.enefti.ui.fragments
//Nama: Mustapha Hadzi
//Kelas: IF-10
//NIM: 10120901
//Tanggal Pengerjaan: 11 Juni 2023
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.enefti.R
import com.example.enefti.data.remote.api.APIService
import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.NFTS
import com.example.enefti.data.repository.OpenSeaRepository
import com.example.enefti.databinding.FragmentCollectionBinding
import com.example.enefti.databinding.FragmentHomeBinding
import com.example.enefti.databinding.FragmentNftBinding
import com.example.enefti.ui.adapters.CollectionAdapter
import com.example.enefti.ui.adapters.NFTAdapter
import com.example.enefti.ui.adapters.TopCollectionAdapter
import com.example.enefti.ui.viewmodels.CollectionViewModel
import com.example.enefti.ui.viewmodels.CollectionViewModelFactory
import com.google.gson.Gson

class NFTFragment : Fragment(R.layout.fragment_nft) {
    private var _binding: FragmentNftBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: CollectionViewModel
    private val TAG = "NFTActivity"
    private val retrofitService = APIService.getInstance()
    private lateinit var detailsButton: Button
    private lateinit var detailsContract: TextView
    private lateinit var detailsContractValue: TextView
    private lateinit var detailsTokenId: TextView
    private lateinit var detailsTokenIDValue: TextView
    private lateinit var detailsTokenType: TextView
    private lateinit var detailsTokenTypeValue: TextView
    private lateinit var detailsChain: TextView
    private lateinit var detailsChainValue: TextView

    val adapter = NFTAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNftBinding.inflate(inflater, container, false)
//        binding.rvnft.adapter = adapter
        viewModel =
            ViewModelProvider(
                this,
                CollectionViewModelFactory(OpenSeaRepository(retrofitService))
            ).get(
                CollectionViewModel::class.java
            )
//        val textView = view?.findViewById<TextView>(R.id.name)
//        textView?.text = data
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.NFTSList.observe(viewLifecycleOwner, Observer {
//            Log.d(TAG, "collectionList: $it")
//            adapter.setNFTList(it)
//        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "errorMessage: $it")
        })
        val data = arguments?.getString("nft")
        val dataCollection = arguments?.getString("collection")
        val collections = Gson().fromJson(dataCollection, Collections::class.java)


        val nft = Gson().fromJson(data, NFTS::class.java)
        val img = loadImageFromIPFS(nft.metadata?.image.toString())
        binding.arrowBack.setOnClickListener{
            findNavController().popBackStack()
        }
        Log.d("test", "masuk $data")
        Glide.with(binding.bannerImg.context).load(collections.banner_image_url).placeholder(R.drawable.collection_example)
                .into(binding.bannerImg)
            Glide.with(binding.nftImg.context).load(img).placeholder(R.drawable.collection_example)
                .into(binding.nftImg)
        if (nft.token_name == null) {
            binding.nftTitle.text = "#"+nft.id
        } else {
            binding.nftTitle.text = nft.token_name
        }
        binding.nftDescription.text = nft.token_description
        detailsButton = binding.detailsButton
        detailsContract = binding.detailsContract
        detailsContractValue = binding.detailsContractValue
        detailsTokenId = binding.detailTokenId
        detailsTokenIDValue = binding.detailsTokenIdValue
        detailsTokenType = binding.detailTokenType
        detailsTokenTypeValue = binding.detailsTokenTypeValue
        detailsChain = binding.detailChain
        detailsChainValue = binding.detailChainValue
        detailsContractValue.text = collections.contract.toList().get(0).contract_address
        detailsTokenIDValue.text = nft.id
        detailsTokenTypeValue.text = nft.token_type
        detailsButton.setOnClickListener {
            toggleDataVisibility()
        }
//        binding.collectionTitle.text = collections.name
//        binding.collectionDescription.text = collections.description
//        binding.collectionVolume.text = collections.stats.total_volume.substringBefore(".")
//        binding.collectionFloor.text = collections.stats.floor_price
//        binding.collectionOwner.text = collections.stats.num_owners
//        binding.rvnft.isNestedScrollingEnabled = false
//        viewModel.getNFTByCollections(collections.contract.firstOrNull()?.contract_address.toString())
    }
    fun loadImageFromIPFS(ipfsCidUrl: String): String? {
        var imageUrl = ipfsCidUrl
        if (!ipfsCidUrl.contains("https")) {
            val ipfsHash = ipfsCidUrl.substringAfter("ipfs://") // Extract the CID from the URL
            imageUrl = "https://ipfs.io/ipfs/$ipfsHash"
        }
        return imageUrl
    }
    private fun toggleDataVisibility() {
        if (detailsContract.visibility == View.GONE) {
            // Show the data
            detailsContract.visibility = View.VISIBLE
            detailsContractValue.visibility = View.VISIBLE
            detailsTokenId.visibility = View.VISIBLE
            detailsTokenIDValue.visibility = View.VISIBLE
            detailsTokenType.visibility = View.VISIBLE
            detailsTokenTypeValue.visibility = View.VISIBLE
            detailsChain.visibility = View.VISIBLE
            detailsChainValue.visibility = View.VISIBLE
            detailsButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.arrow_up, 0)
        } else {
            // Hide the data
            detailsContract.visibility = View.GONE
            detailsContractValue.visibility = View.GONE
            detailsTokenId.visibility = View.GONE
            detailsTokenIDValue.visibility = View.GONE
            detailsTokenType.visibility = View.GONE
            detailsTokenTypeValue.visibility = View.GONE
            detailsChain.visibility = View.GONE
            detailsChainValue.visibility = View.GONE
            detailsButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.arrow_down, 0)
        }
    }
}

package com.example.enefti.ui.fragments
//Nama: Mustapha Hadzi
//Kelas: IF-10
//NIM: 10120901
//Tanggal Pengerjaan: 11 Juni 2023
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.enefti.R
import com.example.enefti.data.remote.api.APIService
import com.example.enefti.data.repository.OpenSeaRepository
import com.example.enefti.databinding.FragmentHomeBinding
import com.example.enefti.databinding.FragmentSearchBinding
import com.example.enefti.ui.adapters.CollectionAdapter
import com.example.enefti.ui.adapters.SearchCollectionAdapter
import com.example.enefti.ui.adapters.TopCollectionAdapter
import com.example.enefti.ui.viewmodels.CollectionViewModel
import com.example.enefti.ui.viewmodels.CollectionViewModelFactory

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    lateinit var viewModel: CollectionViewModel
    private val TAG = "CollectionActivity"
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val retrofitService = APIService.getInstance()
    val adapter = SearchCollectionAdapter()
    private val binding get() = _binding!!
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.rvhighlight.adapter = adapter
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the search query if needed (e.g., trigger API call)
                Log.d("test", "query ${query.toString()}")

                viewModel.searchCollections(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the data based on the search query
                viewModel.searchCollections(newText.toString())
                return true
            }
        })

        //get viewmodel instance using MyViewModelFactory
        viewModel =
            ViewModelProvider(
                this,
                CollectionViewModelFactory(OpenSeaRepository(retrofitService))
            ).get(
                CollectionViewModel::class.java
            )
        viewModel.SearchCollectionsList.observe(viewLifecycleOwner, Observer {
            if (it == null){
                Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()
            } else {
                adapter.setSearchCollectionList(it)
            }
        })

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.SearchCollectionsList.observe(viewLifecycleOwner, Observer {
            if (it == null){
                Toast.makeText(requireContext(),"Data Not Found",Toast.LENGTH_SHORT).show()
            } else {
                adapter.setSearchCollectionList(it)
            }

        })


        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "errorMessage: $it")
        })

        viewModel.searchCollections(" ")
        initClickAdapter()

    }
    fun initClickAdapter() {
        adapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString("collectionAddress", it)
            findNavController().navigate(R.id.navigation_collection, bundle)
        }
    }
}

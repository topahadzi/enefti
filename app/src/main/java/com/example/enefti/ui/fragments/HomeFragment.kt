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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.enefti.R
import com.example.enefti.data.remote.api.APIService
import com.example.enefti.data.repository.OpenSeaRepository
import com.example.enefti.databinding.FragmentHomeBinding
import com.example.enefti.ui.adapters.CollectionAdapter
import com.example.enefti.ui.adapters.TopCollectionAdapter
import com.example.enefti.ui.viewmodels.CollectionViewModel
import com.example.enefti.ui.viewmodels.CollectionViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    lateinit var viewModel: CollectionViewModel
    private val TAG = "CollectionActivity"
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val retrofitService = APIService.getInstance()
    val adapter = CollectionAdapter()
    val topAdapter = TopCollectionAdapter()
    private val binding get() = _binding!!
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        showLoadingLayout()
        binding.rvhighlight.adapter = adapter
        binding.rvrankings.adapter = topAdapter
        spinner = binding.spinner

        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        swipeRefreshLayout = binding.swipeRefreshLayout
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedValue = spinner.selectedItem.toString()
                if (selectedValue == "All") {
                    viewModel.getTopCollections("total_volume", "10")
                }else if (selectedValue == "1d") {
                    viewModel.getTopCollections("one_day_volume", "10")
                }else if (selectedValue == "7d") {
                    viewModel.getTopCollections("seven_day_volume", "10")
                }else if (selectedValue == "30d") {
                    viewModel.getTopCollections("thirty_day_volume", "10")
                }
                viewModel.TopCollectionsList.observe(viewLifecycleOwner, Observer {
                    topAdapter.setCollectionsList(it)
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing when nothing is selected

            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.postDelayed({
                viewModel.collectionsList.observe(viewLifecycleOwner, Observer {
                    adapter.setCollectionsList(it)
                })

                viewModel.TopCollectionsList.observe(viewLifecycleOwner, Observer {
                    topAdapter.setCollectionsList(it)
                })
                viewModel.getCollections()
                viewModel.getTopCollections("total_volume", "10")
                initClickAdapter()
                swipeRefreshLayout.isRefreshing = false

            },2000)
            spinner.setSelection(0)

        }
        //get viewmodel instance using MyViewModelFactory
        viewModel =
            ViewModelProvider(
                this,
                CollectionViewModelFactory(OpenSeaRepository(retrofitService))
            ).get(
                CollectionViewModel::class.java
            )

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.collectionsList.observe(viewLifecycleOwner, Observer {
            showLoadingLayout()
            adapter.setCollectionsList(it)
            hideLoadingLayout()
        })

        viewModel.TopCollectionsList.observe(viewLifecycleOwner, Observer {
            showLoadingLayout()
            topAdapter.setCollectionsList(it)
            hideLoadingLayout()
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "errorMessage: $it")
        })

        viewModel.getCollections()
//        viewModel.getTopCollections("total_volume")
        initClickAdapter()

    }
    private fun showLoadingLayout() {
        binding.loadingTextView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.rvrankings.visibility = View.GONE
        binding.rvhighlight.visibility = View.GONE
        binding.highlightTitle.visibility = View.GONE
        binding.topcollectionTitle.visibility = View.GONE
        binding.collName.visibility = View.GONE
        binding.collTotalName.visibility = View.GONE
        binding.collBar.visibility = View.GONE
        binding.spinner.visibility = View.GONE
    }
    private fun hideLoadingLayout() {
        binding.loadingTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.rvrankings.visibility = View.VISIBLE
        binding.rvhighlight.visibility = View.VISIBLE
        binding.highlightTitle.visibility = View.VISIBLE
        binding.topcollectionTitle.visibility = View.VISIBLE
        binding.collName.visibility = View.VISIBLE
        binding.collTotalName.visibility = View.VISIBLE
        binding.collBar.visibility = View.VISIBLE
        binding.spinner.visibility = View.VISIBLE
    }
    fun initClickAdapter() {
        adapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString("collectionAddress", it)
            findNavController().navigate(R.id.navigation_collection, bundle)
        }
        topAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString("collectionAddress", it)
            findNavController().navigate(R.id.navigation_collection, bundle)
        }
    }
}

package com.inmersoft.trinidadpatrimonial.home.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.HomeFragmentBinding
import com.inmersoft.trinidadpatrimonial.home.ui.adapters.HomeListAdapter
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    private val trinidadDataViewModel: TrinidadDataViewModel by activityViewModels()

    private val homeListAdapter: HomeListAdapter by lazy {
        HomeListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        binding.toolbar.menu.findItem(R.id.action_search)
            .setOnMenuItemClickListener {
                Log.d("TAG", "onCreateView: CLICKED")
                true
            }
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        /*  val searchView = binding.toolbar.searget(1) as androidx.appcompat.widget.SearchView
          searchView.setOnClickListener {
              Log.d("TAG", "userSearch: TESSSTS")
          }*/
/*

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                Toast.makeText(requireContext(), "User search: $query", Toast.LENGTH_SHORT).show()

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d("TAG", "userSearch: $query")
                return true
            }
        })
*/

        binding.fab.setOnClickListener {

            Toast.makeText(requireContext(), "NOT IMPLEMENTED YET!!!", Toast.LENGTH_SHORT).show()

        }

        binding.homeListRecycleview.layoutManager = LinearLayoutManager(requireContext())
        binding.homeListRecycleview.adapter = homeListAdapter
        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(
            viewLifecycleOwner,
            { placeTypeWithPlacesList ->
                homeListAdapter.setData(placeTypeWithPlacesList)
            })

        /* binding.homeListRecycleview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
             override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                 super.onScrolled(recyclerView, dx, dy)
                 if (!recyclerView.canScrollVertically(1)) {
                     Log.d("LOAD-TRINIDAD", "onScrolled: LOAD MORE...")
                 }
             }
         })*/

        //Active the marquee text
        binding.trinidadDesctiptionTxt.isSelected=true

        return binding.root
    }
}


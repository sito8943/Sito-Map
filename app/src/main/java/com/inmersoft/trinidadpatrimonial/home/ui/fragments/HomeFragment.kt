package com.inmersoft.trinidadpatrimonial.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.HomeFragmentBinding
import com.inmersoft.trinidadpatrimonial.home.ui.adapters.HomePlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var mDrawer: DrawerLayout
    private lateinit var mToolbar: MaterialToolbar
    private lateinit var binding: HomeFragmentBinding

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    private val mainAdapter by lazy { HomePlaceTypeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        val recycleTestView: RecyclerView = binding.mainRecycleview
        recycleTestView.adapter = mainAdapter

/*
        val appBarConfiguration =
            AppBarConfiguration(findNavController().graph, binding.drawerLayout)
*/

        /*  mToolbar = binding.toolbar
          val activity = activity as AppCompatActivity?
          activity!!.setSupportActionBar(mToolbar)

          activity!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true);

          mDrawer = binding.drawerLayout
          mDrawerToggle = ActionBarDrawerToggle(
              getActivity(), mDrawer, R.string.drawer_open, R.string.drawer_close
          )

          // Where do I put this?

          // Where do I put this?
          mDrawerToggle.syncState()*/


        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(
            viewLifecycleOwner,
            { placeTypeWithPlacesList ->
                mainAdapter.setData(placeTypeWithPlacesList)
            })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.itemId) {
            android.R.id.home -> {
                mDrawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
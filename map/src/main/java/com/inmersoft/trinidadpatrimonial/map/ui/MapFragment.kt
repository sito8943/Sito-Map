package com.inmersoft.trinidadpatrimonial.map.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ktx.awaitMap
import com.inmersoft.trinidadpatrimonial.map.R
import com.inmersoft.trinidadpatrimonial.map.databinding.MapFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MapFragmentBinding.inflate(inflater, container, false).also {
            (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment).also {
                initMap(it)
            }
        }.root
    }

    private fun initMap(mapFragment: SupportMapFragment?) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            googleMap = mapFragment?.awaitMap()
        }
    }
}
package com.inmersoft.trinidadpatrimonial.details.places.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerDetailAdapter(
    private val fragmentList: List<Fragment>,
    private val fManager: FragmentManager,
    private val lifecycle: Lifecycle
) : FragmentStateAdapter(fManager, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
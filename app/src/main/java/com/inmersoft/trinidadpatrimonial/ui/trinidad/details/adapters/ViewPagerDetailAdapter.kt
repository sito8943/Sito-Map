package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerDetailAdapter(
    private val fManager: FragmentManager,
    private val lifecycle: Lifecycle
) : FragmentStateAdapter(fManager, lifecycle) {

    private val fragmentList = mutableListOf<Fragment>()

    fun setFragments(fragmentListData: List<Fragment>) {
        fragmentList.addAll(0, fragmentListData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
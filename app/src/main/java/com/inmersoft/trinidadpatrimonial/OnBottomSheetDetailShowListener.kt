package com.inmersoft.trinidadpatrimonial

import com.inmersoft.trinidadpatrimonial.details.ui.adapter.ViewPagerDetailFragment

interface OnBottomSheetDetailShowListener {
    fun showDetails(fragmentsList: List<ViewPagerDetailFragment>)
}
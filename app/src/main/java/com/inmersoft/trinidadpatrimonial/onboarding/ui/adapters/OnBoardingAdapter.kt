package com.inmersoft.trinidadpatrimonial.onboarding.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingViewHolder>() {

    private val onboardingImages = mutableListOf<Wallpaper>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnBoardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentWallpaperScreenBinding.inflate(inflater, parent, false)
        return OnBoardingViewHolder(
            binding, androidApiCalls, imageLoader
        )
    }

    fun setData(newWallsList: List<Wallpaper>) {
        wallpaperItems.clear()
        wallpaperItems.addAll(newWallsList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val currentWallpaperItem = wallpaperItems[position]
        holder.bindData(currentWallpaperItem, wallpaperItems.size, position)
    }

    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}

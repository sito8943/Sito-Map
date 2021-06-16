package com.inmersoft.trinidadpatrimonial.onboarding.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.FragmentOnboardingScreenBinding
import com.inmersoft.trinidadpatrimonial.onboarding.data.OnBoardingData


class OnBoardingAdapter(private val onboardingData: List<OnBoardingData>) :
    RecyclerView.Adapter<OnBoardingViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnBoardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentOnboardingScreenBinding.inflate(inflater, parent, false)
        return OnBoardingViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val currentWallpaperItem = onboardingData[position]
        holder.bindData(currentWallpaperItem)
    }

    override fun getItemCount(): Int {
        return onboardingData.size
    }
}

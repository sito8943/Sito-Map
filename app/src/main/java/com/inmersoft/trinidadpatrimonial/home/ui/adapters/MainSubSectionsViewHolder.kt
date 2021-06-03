package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding

class MainSubSectionsViewHolder(val binding: ItemMainPlacesSubsectionsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Funcion para unir lso datos con la UI
    fun bindData(strTest: String) {
        binding.textView3.text = strTest
    }
}
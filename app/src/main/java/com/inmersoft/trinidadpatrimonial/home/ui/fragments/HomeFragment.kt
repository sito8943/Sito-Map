package com.inmersoft.trinidadpatrimonial.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.HomeFragmentBinding
import com.inmersoft.trinidadpatrimonial.home.ui.adapters.MainSectionsAdapter
import com.inmersoft.trinidadpatrimoniald.home.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val recycleTestView: RecyclerView = binding.mainRecycleview

        //TODO ( ESTE ADAPTER ES SOLO PARA MOSTRARLE LA APP A JOSE Y TENER UNA IDEA DE COMO VA A QUEDAR )
        val adapter = MainSectionsAdapter()

        adapter.setData(
            listOf(
                "dqwefwef", "wefwef", "wefwef", "wefwef", "wefwef", "wefwef",
                "wefwef"
            )
        )

        recycleTestView.adapter = adapter

        return binding.root
    }


}
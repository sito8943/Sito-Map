package com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.routes

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.inmersoft.trinidadpatrimonial.database.data.entity.Route
import com.inmersoft.trinidadpatrimonial.databinding.ItemSublistElementBinding
import com.inmersoft.trinidadpatrimonial.extensions.loadImageCenterCropExt
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import java.util.*

class HomeRouteListAdapter(val routeItemOnClick: RouteItemOnClick) :
    ListAdapter<Route, HomeRouteListAdapter.ViewHolder>(RoutesDC()) {

    interface RouteItemOnClick {
        fun showRouteDetails(routeId: Int, sharedTransitionView: View)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeRouteListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSublistElementBinding.inflate(inflater, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(getItem(position))


    inner class ViewHolder(private val binding: ItemSublistElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(route: Route) {
            binding.tvCardHeaderTitle.text = route.route_name
            binding.imCardHeader.loadImageCenterCropExt(Uri.parse(route.header_images[0]))
            TrinidadAssets.getAssetFullPath(
                route.header_images[0],
                TrinidadAssets.jpg
            )
            binding.tvCardSubtitle.text = route.route_description
            binding.cardContainer.transitionName = UUID.randomUUID().toString()
            itemView.setOnClickListener {
                routeItemOnClick.showRouteDetails(route.route_id, binding.cardContainer)
            }
        }
    }


    private class RoutesDC : DiffUtil.ItemCallback<Route>() {
        override fun areItemsTheSame(
            oldItem: Route,
            newItem: Route,
        ): Boolean {
            return oldItem.route_id == newItem.route_id
        }

        override fun areContentsTheSame(
            oldItem: Route,
            newItem: Route,
        ): Boolean {
            return oldItem.route_name == newItem.route_name &&
                    oldItem.route_description == newItem.route_description

        }
    }
}
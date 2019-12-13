package com.example.nearbyplacesapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nearbyplacesapp.R
import com.example.nearbyplacesapp.model.places.CustomA
import com.example.nearbyplacesapp.view.RecyclerViewAdapter.MyViewHolder

class RecyclerViewAdapter(
    private val storesList: List<CustomA>
) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.store_list_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(
            storesList[holder.adapterPosition],
            holder
        )
    }

    override fun getItemCount(): Int {
        return storesList.size
    }

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        private var txtStoreName: TextView = itemView.findViewById<View>(R.id.txtStoreName) as TextView
        private var txtStoreAddr: TextView
        private var txtStoreRating: TextView
        private var txtStoreOpenNow: TextView

        var model: CustomA? = null
        fun setData(info: CustomA, holder: MyViewHolder) {
            holder.txtStoreName.text = info.name
            holder.txtStoreAddr.text = info.vicinity
            holder.txtStoreRating.text = info.rating
            if (info.opening_hours.openNow) holder.txtStoreOpenNow.text =
                "Open now" else holder.txtStoreOpenNow.text = "Closed"

        }

        init {
            txtStoreAddr =
                itemView.findViewById<View>(R.id.txtStoreAddr) as TextView
            txtStoreRating =
                itemView.findViewById<View>(R.id.txtStoreRating) as TextView
            txtStoreOpenNow =
                itemView.findViewById<View>(R.id.txtStoreOpenNow) as TextView
        }
    }

}
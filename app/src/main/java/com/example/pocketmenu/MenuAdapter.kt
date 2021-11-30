package com.example.pocketmenu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private val dataSet: List<String>): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val textView: TextView
        init {
            textView = view.findViewById(R.id.textView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataSet[position]
        holder.textView.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToCategoryMenuFragment(dataSet[position])
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount() = dataSet.size

}
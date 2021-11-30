package com.example.pocketmenu

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class MenuItemsAdapter(private val dataSet: List<MenuItem>):RecyclerView.Adapter<MenuItemsAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtItemName: TextView
        val txtItemPrice: TextView
        init {
            txtItemName = view.findViewById(R.id.txtItemName)
            txtItemPrice = view.findViewById(R.id.txtItemPrice)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_row, parent, false)
        return MenuItemsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemsAdapter.ViewHolder, position: Int) {
        holder.txtItemPrice.text = "@R"+dataSet[position].price.toString()
        holder.txtItemName.text = dataSet[position].itemName
        // add on click listener
        holder.txtItemName.setOnClickListener {
            val action = CategoryMenuFragmentDirections.actionCategoryMenuFragmentToOrderMenuItemFragment(dataSet[position])
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount() = dataSet.size

}
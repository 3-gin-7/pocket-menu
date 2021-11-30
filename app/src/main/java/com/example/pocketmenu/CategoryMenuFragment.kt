package com.example.pocketmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CategoryMenuFragment : Fragment() {
    // logging
    private var TAG = "CatMenu"
    // view vars
    private lateinit var txtCategory: TextView
    private lateinit var lstMenuItems: RecyclerView
    // args
    val args: CategoryMenuFragmentArgs by navArgs()
    // firestore
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = args.CategoryName
        val menuItemList: MutableList<MenuItem> = arrayListOf()
        val linearLayoutManager = LinearLayoutManager(context)
        var dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)

        lstMenuItems = view.findViewById(R.id.lstMenuItems)
        lstMenuItems.addItemDecoration(dividerItemDecoration)
        lstMenuItems.layoutManager = linearLayoutManager


        txtCategory = view.findViewById(R.id.txtCategory)
        txtCategory.text = category

        db.collection("menuItems").whereEqualTo("category",category).get()
            .addOnSuccessListener { docs ->
                Log.i(TAG, "$docs")
                for(doc in docs){
                    val item = doc.toObject(MenuItem::class.java)
                    menuItemList.add(item)
                }
                lstMenuItems.adapter = MenuItemsAdapter(menuItemList)
            }
    }

    companion object {
        @JvmStatic fun newInstance(param1: String, param2: String) =
                CategoryMenuFragment().apply {
                }
    }
}
package com.example.pocketmenu

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    // logging
    private val TAG = "Home"
    // firestore
    private val db = Firebase.firestore
    // view vars
    private lateinit var recycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        var dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
        recycler = view.findViewById(R.id.lstMenu)
        recycler.addItemDecoration(dividerItemDecoration)
        recycler.layoutManager = linearLayoutManager

        MainActivity.progressBar.isVisible = true
        db.collection("menu").document("categories").get()
            .addOnSuccessListener { document ->
                if(document != null){
                    val menu = document.get("categories") as List<String>
                    Log.i(TAG, "" +menu)
                    recycler.adapter = MenuAdapter(menu)
                }
                MainActivity.progressBar.isVisible = false
            }.addOnFailureListener {
                Log.d(TAG, "Exception: " + it.message)
                MainActivity.progressBar.isVisible = false
            }
        db.collection("activeOrders").whereEqualTo("userId", Cart.userId).get()
            .addOnSuccessListener {
                for(doc in it){
                    Cart.orderNumber = doc.id
                    Cart.orderPlaced = true
                    Log.i(TAG, Cart.orderNumber)
                }
            }.addOnFailureListener{
                Log.w(TAG, "Error while fetching orderId", it)
            }
        Log.i(TAG, "OnViewCreated")
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
            }
    }
}
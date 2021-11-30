package com.example.pocketmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderMenuItemFragment : Fragment() {
    // logging
    private val TAG = "OrderMenuItem"
    //firestore
    // args
    val args: OrderMenuItemFragmentArgs by navArgs()
    // firestore
    private val db = Firebase.firestore
    //view vars
    private lateinit var txtItemName:TextView
    private lateinit var edtItemQty: EditText
    private lateinit var txtPrice: TextView
    private lateinit var btnAddOrder: Button

    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_menu_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuItem = args.menuItem

        txtItemName = view.findViewById(R.id.txtOrderItemName)
        txtPrice = view.findViewById(R.id.txtOrderItemPrice)
        edtItemQty = view.findViewById(R.id.edtOrderItemQuantity)
        btnAddOrder = view.findViewById(R.id.btnAddOrder)

        edtItemQty.setText("0")
        txtPrice.text = "@R${menuItem.price}"

        btnAddOrder.setOnClickListener(addOrderListener)
    }

    private val addOrderListener = View.OnClickListener {
        val qty = edtItemQty.text.toString().toInt()
        if(qty > 0) {
            Cart.orderList.add(Cart.Order(txtItemName.text.toString(), qty, menuItem.price, menuItem.category +""))
            var badge = MainActivity.navigation.getOrCreateBadge(R.id.cartFragment)
            badge.isVisible = true
            badge.number = Cart.orderList.size
            Navigation.findNavController(requireView()).navigateUp()
            Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), "Quantity must be more that 0", Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderMenuItemFragment().apply {
            }
    }
}
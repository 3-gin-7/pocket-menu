package com.example.pocketmenu

import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class CartFragment : Fragment() {
    // view vars
    private lateinit var txtOrderList: TextView
    private lateinit var rgrpCollection: RadioGroup
    private lateinit var rbtnSelfCollection: RadioButton
    private lateinit var rbtnDelivery: RadioButton
    private lateinit var edtAddress : TextView
    private lateinit var rgrpTip: RadioGroup
    private lateinit var rbtnNoTip: RadioButton
    private lateinit var rbtn5Tip: RadioButton
    private lateinit var rbtn10Tip: RadioButton
    private lateinit var rbtnCustom: RadioButton
    private lateinit var edtCustomTip: TextView
    private lateinit var txtTotalOrder: TextView
    private lateinit var btnPlaceOrder: Button

    // cart vars
    private var ogTotal = 0.0
    private var total = 0.0
    private var tip = 0.0
    private var orderAddress = ""
    private val TAG = "Cart"
    private var collectionMethod = "collect"

    //firebase
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(Cart.orderPlaced){
            Log.i(TAG, "order is placed")
            Navigation.findNavController(requireView()).navigate(R.id.action_cartFragment_to_activeOrderFragment)
        }
        if(Cart.orderList.size == 0){
            // show nothing to show
            return
        }
        txtOrderList = view.findViewById(R.id.txtOrderList)
        rgrpCollection = view.findViewById(R.id.rgrpCollection)
        rbtnSelfCollection = view.findViewById(R.id.rbtnSelfCollection)
        rbtnDelivery = view.findViewById(R.id.rbtnDelivery)
        edtAddress = view.findViewById(R.id.edtAddress)
        rgrpTip = view.findViewById(R.id.rgrpTip)
        rbtnNoTip = view.findViewById(R.id.rbtnNoTip)
        rbtn5Tip = view.findViewById(R.id.rbtn5Tip)
        rbtn10Tip = view.findViewById(R.id.rbtn10Tip)
        rbtnCustom= view.findViewById(R.id.rbtnCustom)
        edtCustomTip = view.findViewById(R.id.edtCustomTip)
        txtTotalOrder = view.findViewById(R.id.txtOrderTotal)
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder)

        var orderList = ""
        for(order in Cart.orderList){
            orderList += order.orderName + "\tx${order.orderQty.toInt()} @R${order.itemPrice.toInt()}\n"
        }
        txtOrderList.text = orderList

        rbtnSelfCollection.isChecked = true
        edtAddress.isFocusable = false
        edtAddress.isFocusableInTouchMode = false
        edtAddress.inputType = InputType.TYPE_NULL

        rbtnNoTip.isChecked = true
        edtCustomTip.text = "0"
        edtCustomTip.isFocusable = false
        edtCustomTip.isFocusableInTouchMode = false
        edtCustomTip.inputType = InputType.TYPE_NULL

        ogTotal = Cart.orderList.sumByDouble { it.itemPrice * it.orderQty }
        total = ogTotal

        txtTotalOrder.text = "R${round(ogTotal)}"

        rgrpCollection.setOnCheckedChangeListener(collectionListener)
        rgrpTip.setOnCheckedChangeListener(tipListener)

        edtCustomTip.onFocusChangeListener = customTipFocusChange

        btnPlaceOrder.setOnClickListener(placeOrderListener)

    }

    private val collectionListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        when(checkedId){
            R.id.rbtnSelfCollection -> {
                edtAddress.isFocusable = false
                edtAddress.isFocusableInTouchMode = false
                edtAddress.inputType = InputType.TYPE_NULL
                collectionMethod = "collect"
            }
            R.id.rbtnDelivery ->{
                edtAddress.isFocusable = true
                edtAddress.isFocusableInTouchMode = true
                edtAddress.inputType = InputType.TYPE_CLASS_TEXT
                collectionMethod = "delivery"
            }
        }
    }

    private val tipListener = RadioGroup.OnCheckedChangeListener{ group, checkedId ->
        when(checkedId){
            R.id.rbtnNoTip -> {
                tip = 0.0
                disableTip()
                updateTotalAndView()
            }
            R.id.rbtn5Tip -> {
                tip = 5.0
                disableTip()
                updateTotalAndView()
            }
            R.id.rbtn10Tip -> {
                tip = 10.0
                disableTip()
                updateTotalAndView()
            }
            R.id.rbtnCustom -> {
                edtCustomTip.isFocusable = true
                edtCustomTip.isFocusableInTouchMode = true
                edtCustomTip.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }

    private fun disableTip(){
        edtCustomTip.isFocusable = false
        edtCustomTip.isFocusableInTouchMode = false
        edtCustomTip.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun updateTotalAndView(){
        total = if(tip == 0.0) ogTotal
        else ogTotal * (1 + (tip/100.0))

        txtTotalOrder.text = "${round(total)}"
    }

    private val customTipFocusChange = View.OnFocusChangeListener { v, hasFocus ->
        if(!hasFocus){
            val tipInput = edtCustomTip.text.toString().toDoubleOrNull()
            if(tipInput == null){
                Toast.makeText(requireContext(), "Incorrect Input", Toast.LENGTH_SHORT).show()
                tip = 0.0
                rbtnNoTip.isChecked = true
            }else{
                tip = tipInput
                updateTotalAndView()
            }
        }
    }

    private val placeOrderListener = View.OnClickListener {
        if(collectionMethod == "delivery" && edtAddress.text.toString().isNullOrEmpty()){
            Toast.makeText(requireContext(), "Address is empty", Toast.LENGTH_LONG).show()
        }else{
            if(collectionMethod == "delivery") orderAddress = edtAddress.text.toString()
            else orderAddress = ""
            val order = ClientOrder(
                auth.currentUser!!.uid,
                Cart.orderList,
                round(total),
                round(total* (tip/100)),
                collectionMethod,
                orderAddress,
                "Preparing",
                    System.currentTimeMillis() / 1000L
            )
            db.collection("activeOrders").add(order).addOnSuccessListener {
                Log.i(TAG, "Order Placed")
                Toast.makeText(requireContext(), "Order Placed", Toast.LENGTH_SHORT).show()
                Cart.orderNumber = it.id
                Cart.orderPlaced = true
                Navigation.findNavController(requireView()).navigate(R.id.action_cartFragment_to_activeOrderFragment)
            }.addOnFailureListener{
                Log.w(TAG, "Error placing order", it)
                Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_LONG).show()
            }
            var badge = MainActivity.navigation.getOrCreateBadge(R.id.cartFragment)
            badge.isVisible = false
            badge.number = 0
            Log.i(TAG, order.toString())
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
            }
    }
}
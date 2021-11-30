package com.example.pocketmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId

class ActiveOrderFragment : Fragment() {
    // logging
    private val TAG = "ActiveOrder"
    // view vars
    private lateinit var txtOrderNumber: TextView
    private lateinit var txtReceived: TextView
    private lateinit var txtStatus: TextView

    //firestore
    private val db =Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtOrderNumber = view.findViewById(R.id.txtOrderNumber)
        txtReceived = view.findViewById(R.id.txtReceived)
        txtStatus = view.findViewById(R.id.txtStatus)

        val orderRef = db.collection("activeOrders").document(Cart.orderNumber)
        orderRef.addSnapshotListener{ snapshot, e ->
            if(e != null){
                Log.w(TAG, "Listener failed",e)
                return@addSnapshotListener
            }
            if(snapshot != null && snapshot.exists()){
                val clientOrder = snapshot.toObject(ClientOrder::class.java)
                Log.i(TAG, clientOrder.toString())
                txtOrderNumber.text = snapshot.id
                if(clientOrder != null){
                    val status = clientOrder.orderStatus
                    if(status == "Collected"){
                        Log.i(TAG, "Order is marked collected")
                        Cart.orderPlaced = false
                        Cart.orderNumber = ""
                        Cart.orderList.clear()
                    }
                    txtStatus.text =status
                    val stamp = Timestamp(clientOrder.orderOpenedDate * 1000)

                    txtReceived.text = Date(stamp.time).toString() +" "+ Time(stamp.time).toString()
                }
            }else{
                Log.d(TAG, "Current data is null")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ActiveOrderFragment().apply {
                }
    }
}
package com.example.pocketmenu

import com.google.type.DateTime

data class ClientOrder(
    val userId: String ="",
    val items: List<Cart.Order> = arrayListOf(),
    val total: Double = 0.0,
    val tip: Double = 0.0,
    val collection: String = "",
    val address: String = "",
    val orderStatus: String = "",
    val orderOpenedDate: Long = 0,
    val orderClosedDate: Long = 0
    ){
}

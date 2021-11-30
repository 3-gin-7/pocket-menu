package com.example.pocketmenu

class Cart {
    data class Order(
        val orderName: String = "",
        val orderQty: Int = 0,
        val itemPrice: Double = 0.0,
        val itemCategory: String = ""
    )

    companion object{
        val orderList: MutableList<Order> = arrayListOf()
        var orderPlaced = false
        var userId = ""
        var orderNumber = ""
    }
}
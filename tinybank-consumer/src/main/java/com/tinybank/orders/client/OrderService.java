package com.tinybank.orders.client;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

import com.tinybank.orders.model.Order;

public interface OrderService {
	
    @GET("orders")
    Call<List<Order>> getOrders();
}

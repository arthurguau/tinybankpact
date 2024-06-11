package com.tinybank.orders.client;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import com.tinybank.orders.model.Order;

public class OrderApiClient {
	
    private final OrderService accountService;

    public OrderApiClient(String url) {
    	
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        accountService = retrofit.create(OrderService.class);
    }

    public List<Order> fetchOrders() throws IOException {
        Response<List<Order>> response = accountService.getOrders().execute();
        return response.body();
    }
}

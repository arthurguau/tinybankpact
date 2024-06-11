package com.tinybank.orders;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.tinybank.orders.client.OrderApiClient;

import java.io.IOException;
import java.util.Map;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.*;

@ExtendWith(PactConsumerTestExt.class)
public class OrderApiClientPactTest {
	
	/**
	 * 
	 * @param builder
	 * @return
	 */
    @Pact(provider = "tinybank-provider", consumer = "tinybank-consumer")
    public V4Pact listOfAccountsPact(PactDslWithProvider builder) {
    
    	return builder
                .given("there are accounts")
                .uponReceiving("a request for accounts")
                .path("/accounts")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(newJsonArrayMinLike(1, a -> a.object(o -> {
                    o.id("partyId");
                    o.eachLike("accounts", i -> {
                        i.stringType("type");
                        i.numberType("number");
                        //i.numberType("amount");
                    });
                })).build())
                .toPact(V4Pact.class);
    }

    /**
     * 
     * @param builder
     * @return
     */
    @Pact(provider = "tinybank-provider", consumer = "tinybank-consumer")
    public V4Pact noAccountsPact(PactDslWithProvider builder) {
        
    	return builder
                .given("there are no accounts")
                .uponReceiving("a request for accounts")
                .path("/accounts")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body("[]")
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "listOfAccountsPact")
    void getListOfAccounts(MockServer mockServer) throws IOException {
    	
        var client = new OrderApiClient(mockServer.getUrl());
        var orders = client.fetchOrders();
        
        Assertions.assertNotNull(orders);
        Assertions.assertFalse(orders.isEmpty());
        Assertions.assertNotNull(orders.get(0).getAccounts());
        Assertions.assertFalse(orders.get(0).getAccounts().isEmpty());
    }

    @Test
    @PactTestFor(pactMethod = "noAccountsPact")
    void getEmptyListOfAccounts(MockServer mockServer) throws IOException {
    	
        var client = new OrderApiClient(mockServer.getUrl());
        var accounts = client.fetchOrders();
        
        Assertions.assertNotNull(accounts);
        Assertions.assertTrue(accounts.isEmpty());
    }
}

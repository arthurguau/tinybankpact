package com.tinybank.orders;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinybank.orders.model.Order;
import com.tinybank.orders.repository.OrdersRepository;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("tinybank-provider")
@PactBroker
public class ContractVerificationTest {
	
    @MockBean
    private OrdersRepository ordersRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("there are accounts")
    public void thereAreOrders() throws IOException {
        Mockito.when(ordersRepository.getOrders()).thenReturn(getOrdersFromFile("orders.json"));
    }

    @State("there are no accounts")
    public void thereAreNoOrders() throws IOException {
        Mockito.when(ordersRepository.getOrders()).thenReturn(getOrdersFromFile("no_orders.json"));
    }

    private List<Order> getOrdersFromFile(String filename) throws IOException {
        URL resource = getClass().getClassLoader().getResource(filename);
        return objectMapper.readValue(resource, new TypeReference<>() {
        });
    }
}

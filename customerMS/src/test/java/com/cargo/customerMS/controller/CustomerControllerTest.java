package com.cargo.customerMS.controller;

import com.cargo.customerMS.entity.Customer;
import com.cargo.customerMS.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    private CustomerService customerService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new CustomerController(customerService)).build();
    }

    @Test
    void addCustomerReturnsCreatedCustomer() throws Exception {
        Customer savedCustomer = new Customer("Avery", 31);
        savedCustomer.setId(1L);
        when(customerService.addCustomer(org.mockito.ArgumentMatchers.any(Customer.class)))
                .thenReturn(savedCustomer);

        mockMvc.perform(post("/api/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Avery\",\"age\":31}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Avery"))
                .andExpect(jsonPath("$.age").value(31));
    }

    @Test
    void addCustomerRejectsMalformedJson() throws Exception {
        mockMvc.perform(post("/api/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not-json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomerByIdReturnsCustomer() throws Exception {
        Customer customer = new Customer("Morgan", 26);
        customer.setId(4L);
        when(customerService.getCustomerById(4L)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Morgan"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    void getCustomerByIdReturnsNotFoundWhenServiceCannotFindCustomer() throws Exception {
        when(customerService.getCustomerById(404L))
                .thenThrow(new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Customer not found"));

        mockMvc.perform(get("/api/customers/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerByIdRejectsNonNumericId() throws Exception {
        mockMvc.perform(get("/api/customers/not-a-number"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCustomersReturnsCustomerList() throws Exception {
        Customer customer = new Customer("Taylor", 40);
        customer.setId(8L);
        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(8))
                .andExpect(jsonPath("$[0].name").value("Taylor"));
    }

    @Test
    void getAllCustomersReturnsEmptyArrayWhenNoCustomersExist() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        mockMvc.perform(get("/api/customers/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}

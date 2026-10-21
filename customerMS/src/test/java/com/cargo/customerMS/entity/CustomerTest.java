package com.cargo.customerMS.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void settersUpdateCustomerFields() {
        Customer customer = new Customer("Before", 20);

        customer.setName("After");
        customer.setAge(21);

        assertEquals("After", customer.getName());
        assertEquals(21, customer.getAge());
    }
}

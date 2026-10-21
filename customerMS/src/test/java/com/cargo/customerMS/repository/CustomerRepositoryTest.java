package com.cargo.customerMS.repository;

import com.cargo.customerMS.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void clearCustomers() {
        customerRepository.deleteAll();
    }

    @Test
    void saveGeneratesIdAndPersistsCustomer() {
        Customer savedCustomer = customerRepository.save(new Customer("Casey", 29));

        assertNotNull(savedCustomer.getId());
        Optional<Customer> retrieved = customerRepository.findById(savedCustomer.getId());
        assertFalse(retrieved.isEmpty());
        assertEquals("Casey", retrieved.get().getName());
        assertEquals(29, retrieved.get().getAge());
    }

    @Test
    void findByIdReturnsEmptyWhenCustomerDoesNotExist() {
        assertFalse(customerRepository.findById(Long.MAX_VALUE).isPresent());
    }

    @Test
    void findAllReturnsPersistedCustomers() {
        customerRepository.saveAll(List.of(
                new Customer("Jamie", 24),
                new Customer("Drew", 33)));

        assertEquals(2, customerRepository.findAll().size());
    }
}

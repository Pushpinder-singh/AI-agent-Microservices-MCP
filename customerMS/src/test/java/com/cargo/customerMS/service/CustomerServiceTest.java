package com.cargo.customerMS.service;

import com.cargo.customerMS.entity.Customer;
import com.cargo.customerMS.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void addCustomerClearsProvidedIdAndReturnsSavedCustomer() {
        Customer customer = new Customer("Riley", 28);
        customer.setId(99L);
        Customer savedCustomer = new Customer("Riley", 28);
        savedCustomer.setId(1L);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        Customer result = customerService.addCustomer(customer);

        assertEquals(1L, result.getId());
        assertEquals("Riley", result.getName());
        assertEquals(28, result.getAge());
        assertEquals(null, customer.getId());
        verify(customerRepository).save(customer);
    }

    @Test
    void getCustomerByIdReturnsExistingCustomer() {
        Customer customer = new Customer("Jordan", 35);
        customer.setId(7L);
        when(customerRepository.findById(7L)).thenReturn(Optional.of(customer));

        assertSame(customer, customerService.getCustomerById(7L));
    }

    @Test
    void getCustomerByIdThrowsNotFoundWhenCustomerDoesNotExist() {
        when(customerRepository.findById(404L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> customerService.getCustomerById(404L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getAllCustomersReturnsRepositoryResults() {
        List<Customer> customers = List.of(new Customer("Sam", 22));
        when(customerRepository.findAll()).thenReturn(customers);

        assertSame(customers, customerService.getAllCustomers());
    }

    @Test
    void getAllCustomersReturnsEmptyListWhenRepositoryIsEmpty() {
        when(customerRepository.findAll()).thenReturn(List.of());

        assertEquals(List.of(), customerService.getAllCustomers());
    }
}

package com.cargo.customerMS.repository;

import com.cargo.customerMS.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

package com.example.core_banking.repository;

import com.example.core_banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByIdentityNumber(String identityNumber);
    boolean existsByEmail(String email);
}

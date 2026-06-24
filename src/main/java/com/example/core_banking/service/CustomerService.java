package com.example.core_banking.service;

import com.example.core_banking.dto.CreateCustomerRequest;
import com.example.core_banking.entity.Customer;
import com.example.core_banking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(CreateCustomerRequest request) {

        if (customerRepository.existsByIdentityNumber(request.identityNumber())) {
            throw new RuntimeException("There is already a customer registered with that TCKN!");
        }
        if (customerRepository.existsByEmail(request.email())) {
            throw new RuntimeException("This email already in use!");
        }

        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setIdentityNumber(request.identityNumber());
        customer.setEmail(request.email());

        return customerRepository.save(customer);
    }

}

package edu.springframework.spring6customerassignment.service;

import edu.springframework.spring6customerassignment.model.Customer;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(UUID id);

    Customer save(Customer customer);

    void update(UUID uuid, Customer customer);

    void deleteById(UUID uuid);

    void patchCustomer(UUID customerId, Customer customer);

}

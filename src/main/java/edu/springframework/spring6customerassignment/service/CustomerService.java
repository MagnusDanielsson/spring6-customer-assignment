package edu.springframework.spring6customerassignment.service;

import edu.springframework.spring6customerassignment.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> findAll();
    Optional<CustomerDTO> findById(UUID id);

    CustomerDTO save(CustomerDTO customer);

    Optional<CustomerDTO> update(UUID uuid, CustomerDTO customer);

    void deleteById(UUID uuid);

    Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customer);

}

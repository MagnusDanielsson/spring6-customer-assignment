package edu.springframework.spring6customerassignment.repositories;

import edu.springframework.spring6customerassignment.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}

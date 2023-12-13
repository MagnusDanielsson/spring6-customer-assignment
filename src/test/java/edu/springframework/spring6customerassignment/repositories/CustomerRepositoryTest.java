package edu.springframework.spring6customerassignment.repositories;

import edu.springframework.spring6customerassignment.entities.Customer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Rollback
    @Transactional
    @Test
    void testSaveCustomer() {
        Customer saveCustomer =
                Customer.builder().
                        customerName("Mange").
                        createdDate(LocalDateTime.now()).
                        lastModifiedDate(LocalDateTime.now()).
                        version(1).build();

        Customer customer = customerRepository.save(saveCustomer);
        assertThat(customer.getCustomerId()).isNotNull();
    }

}
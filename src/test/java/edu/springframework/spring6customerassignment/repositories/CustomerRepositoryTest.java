package edu.springframework.spring6customerassignment.repositories;

import edu.springframework.spring6customerassignment.entities.Customer;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testCustomerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Customer saveCustomer =
                    Customer.builder().
                            customerName("laksjdakhfafhafhaöihföaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaisfhjdaöfdhjaöHFIAHFHJ").
                            createdDate(LocalDateTime.now()).
                            lastModifiedDate(LocalDateTime.now()).
                            version(1).build();

            Customer customer = customerRepository.save(saveCustomer);
            customerRepository.flush();
        });
    }

    @Rollback
    @Transactional
    @Test
    void testSaveCustomer() {
        Customer saveCustomer =
                Customer.builder().
                        customerName("Magnus").
                        createdDate(LocalDateTime.now()).
                        lastModifiedDate(LocalDateTime.now()).
                        version(1).build();

        Customer customer = customerRepository.save(saveCustomer);
        customerRepository.flush();
        assertThat(customer.getCustomerId()).isNotNull();
    }

}
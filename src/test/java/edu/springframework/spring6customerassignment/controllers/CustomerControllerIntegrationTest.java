package edu.springframework.spring6customerassignment.controllers;

import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.exception.NotFoundException;
import edu.springframework.spring6customerassignment.mappers.CustomerMapper;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import edu.springframework.spring6customerassignment.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIntegrationTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void getCustomerById(){
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity<CustomerDTO> responseEntity = customerController.getCustomerById(customer.getCustomerId());
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void getCustomerNotFound() {
        UUID uuid = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(uuid));
    }

    @Test
    void getAllCustomers() {
        List<CustomerDTO> customerList = customerController.getAllCustomers();
        assertThat(customerList.size()).isEqualTo(4);
    }

    @Rollback
    @Transactional
    @Test
    void createCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().customerName("Create Customer").build();

        ResponseEntity<CustomerDTO> created = customerController.createCustomer(customerDTO);
        CustomerDTO createdCustomerDTO = created.getBody();
        assertThat(createdCustomerDTO.getCustomerName()).isEqualTo(customerDTO.getCustomerName());
    }

    @Rollback
    @Transactional
    @Test
    void updateCustomer() {
        Customer customer = customerRepository.findAll().get(0);

    }

    @Rollback
    @Transactional
    @Test
    void patchCustomerTest() {
        CustomerDTO customerDto = customerMapper.customerToCustomerDto(customerRepository.findAll().get(0));
        final String name = "UPDATED-NAME";
        customerDto.setCustomerName(name);

        ResponseEntity responseEntity = customerController.patchCustomer(customerDto.getCustomerId(), customerDto);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    }

    @Test
    void patchCustomerNotFound() {
        CustomerDTO dummy = CustomerDTO.builder().build();
        assertThrows(NotFoundException.class, () -> customerController.patchCustomer(UUID.randomUUID(), dummy));
    }

}


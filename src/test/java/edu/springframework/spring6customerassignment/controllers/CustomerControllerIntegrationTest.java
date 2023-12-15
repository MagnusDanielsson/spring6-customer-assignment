package edu.springframework.spring6customerassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.exception.NotFoundException;
import edu.springframework.spring6customerassignment.mappers.CustomerMapper;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import edu.springframework.spring6customerassignment.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerIntegrationTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllCustomersTest() {
        List<CustomerDTO> customerList = customerController.getAllCustomers();
        assertThat(customerList.size()).isEqualTo(4);
    }

    @Test
    void getCustomerByIdTest(){
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity<CustomerDTO> responseEntity = customerController.getCustomerById(customer.getCustomerId());
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void getCustomerNotFoundTest() {
        UUID uuid = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(uuid));
    }

    @Rollback
    @Transactional
    @Test
    void createCustomerTest() {
        CustomerDTO customerDTO = CustomerDTO.builder().customerName("Create Customer").build();

        ResponseEntity<CustomerDTO> created = customerController.createCustomer(customerDTO);
        CustomerDTO createdCustomerDTO = created.getBody();
        Optional<Customer> customer = customerRepository.findById(createdCustomerDTO.getCustomerId());
        assertThat(customer).isNotEqualTo(Optional.empty());
    }

    @Rollback
    @Transactional
    @Test
    void updateCustomerTest() {
        CustomerDTO customerDto = customerMapper.customerToCustomerDto(customerRepository.findAll().get(0));
        final String name = "UPDATED-NAME";
        customerDto.setCustomerName(name);

        ResponseEntity responseEntity = customerController.updateCustomer(customerDto.getCustomerId(), customerDto);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        Optional<Customer> customer = customerRepository.findById(customerDto.getCustomerId());
        assertThat(customer.get().getCustomerName()).isEqualTo(name);
    }

    @Test
    void updateCustomerNotFoundTest() {
        assertThrows(NotFoundException.class, () -> customerController.updateCustomer(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void patchCustomerTest() {
        CustomerDTO customerDto = customerMapper.customerToCustomerDto(customerRepository.findAll().get(0));
        final String name = "PATCHED-NAME";
        customerDto.setCustomerName(name);

        ResponseEntity responseEntity = customerController.patchCustomer(customerDto.getCustomerId(), customerDto);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        Optional<Customer> customer = customerRepository.findById(customerDto.getCustomerId());
        assertThat(customer.get().getCustomerName()).isEqualTo(name);
    }

    @Test
    void patchCustomerNotFoundTest() {
        CustomerDTO dummy = CustomerDTO.builder().build();
        assertThrows(NotFoundException.class, () -> customerController.patchCustomer(UUID.randomUUID(), dummy));
    }

    @Test
    void testPatchCustomerBadRequest() throws Exception {
        Customer customer = customerRepository.findAll().get(0);

        HashMap<String, String> customerMap = new HashMap<>();
        customerMap.put("customerName", "Carl Michael Bellman ladaöllwjd oadjkfaäjfda pakdakd aälksdakjd aokdoa ldkd");

        mockMvc.perform(patch("/api/v1/customer/" + customer.getCustomerId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                        .andExpect(jsonPath("$.size()", is(1)))
                        .andExpect(status().isBadRequest());

    }

}


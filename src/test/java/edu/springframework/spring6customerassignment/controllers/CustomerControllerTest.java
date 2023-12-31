package edu.springframework.spring6customerassignment.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.mappers.CustomerMapper;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import edu.springframework.spring6customerassignment.repositories.CustomerRepository;
import edu.springframework.spring6customerassignment.service.CustomerService;
import edu.springframework.spring6customerassignment.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CustomerService customerService;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;
    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
       customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void getAllCustomers() throws Exception {

        given(customerService.findAll()).willReturn(customerServiceImpl.findAll());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(customerServiceImpl.findAll().size())))
                .andExpect(jsonPath("$[0].customerId", is(customerServiceImpl.findAll().get(0).getCustomerId().toString())))
                .andExpect(jsonPath("$[0].customerName", is(customerServiceImpl.findAll().get(0).getCustomerName())))
                .andExpect(jsonPath("$[1].customerId", is(customerServiceImpl.findAll().get(1).getCustomerId().toString())))
                .andExpect(jsonPath("$[1].customerName", is(customerServiceImpl.findAll().get(1).getCustomerName())));

    }

    @Test
    void getCustomerById() throws Exception {

        CustomerDTO customer = customerServiceImpl.findAll().get(0);

        given(customerService.findById(customer.getCustomerId())).willReturn(Optional.of(customer));

        mockMvc.perform(get("/api/v1/customer/" + customer.getCustomerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(customer.getCustomerId().toString())))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));

        verify(customerService).findById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getCustomerId());
    }

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.findAll().get(0);

        HashMap<String, String> customerMap = new HashMap<>();
        customerMap.put("customerName", "Carl Michael Bellman");

        given(customerService.patchCustomer(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(patch("/api/v1/customer/" + customer.getCustomerId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                        .andExpect(status().isOk());


        verify(customerService).patchCustomer(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getCustomerId());
        assertThat(customerArgumentCaptor.getValue().getCustomerName()).isEqualTo(customerMap.get("customerName"));
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.findAll().get(0);
        customer.setCustomerId(null);
        customer.setVersion(null);

        given(customerService.save(any(CustomerDTO.class))).willReturn(customerServiceImpl.findAll().get(1));

        mockMvc.perform(post("/api/v1/customer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                        .andExpect(status().isCreated())
                        .andExpect(header().exists("Location"));

        verify(customerService).save(customerArgumentCaptor.capture());
        CustomerDTO captorValue = customerArgumentCaptor.getValue();
        assertThat(captorValue.getCustomerId()).isNull();
        assertThat(captorValue.getVersion()).isNull();
    }


    @Test
    void testCreateCustomerNameIsNull() throws Exception {
        CustomerDTO customerDto = CustomerDTO.builder().build();

        given(customerService.save(any(CustomerDTO.class))).willReturn(customerServiceImpl.findAll().get(1));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto)))
                        .andExpect(jsonPath("$.size()", is(2)))
                        .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.findAll().get(0);

        given(customerService.update(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(put("/api/v1/customer/" + customer.getCustomerId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                        .andExpect(status().isOk());

        verify(customerService).update(any(UUID.class), any(CustomerDTO.class));

    }


    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.findAll().get(0);

        given(customerService.deleteById(any())).willReturn(Boolean.TRUE);

        mockMvc.perform(delete("/api/v1/customer/" + customer.getCustomerId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        verify(customerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getCustomerId());
    }

}


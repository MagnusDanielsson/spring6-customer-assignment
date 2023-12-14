package edu.springframework.spring6customerassignment.controllers;

import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.exception.NotFoundException;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import edu.springframework.spring6customerassignment.service.CustomerService;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(path = CUSTOMER_PATH)
    public List<CustomerDTO> getAllCustomers() {
        log.info("CustomerController: Getting all customers");
        return customerService.findAll();
    }

    @GetMapping(path = CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.info("CustomerController: Getting customer by id: " + customerId);
        Optional<CustomerDTO> existing = customerService.findById(customerId);
        if(existing.isEmpty()) {
            throw new NotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + existing.get().getCustomerId());
        return new ResponseEntity<>(existing.get(), headers, HttpStatus.OK);
    }

    @PostMapping(path = CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        log.info("CustomerController: Creating customer: " + customer);
        CustomerDTO savedCustomer = customerService.save(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getCustomerId());
        return new ResponseEntity<>(savedCustomer, headers, HttpStatus.CREATED);
    }

    @PutMapping(path = CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        log.info("CustomerController: Updating customer: " + customer);
        Optional<CustomerDTO> updatedCustomer = customerService.update(customerId, customer);
        if (updatedCustomer.isEmpty()) {
            throw new NotFoundException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + customer.getCustomerId());

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PatchMapping(path = CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        log.info("CustomerController: Patching customer: " + customer);
        Optional<CustomerDTO> patchedCustomer = customerService.patchCustomer(customerId, customer);
        if(patchedCustomer.isEmpty()) {
            throw new NotFoundException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + patchedCustomer.get().getCustomerId());

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @DeleteMapping(path = CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        log.info("CustomerController: Deleting customer by id: " + customerId);
        if( !customerService.deleteById(customerId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}

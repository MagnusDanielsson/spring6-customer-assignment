package edu.springframework.spring6customerassignment.controllers;

import edu.springframework.spring6customerassignment.exception.NotFoundException;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import edu.springframework.spring6customerassignment.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @PostMapping(path = CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        log.info("CustomerController: Creating customer: " + customer);
        CustomerDTO savedCustomer = customerService.save(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getCustomerId());
        return new ResponseEntity<>(savedCustomer, headers, HttpStatus.CREATED);
    }

    @PutMapping(path = CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        log.info("CustomerController: Updating customer: " + customer);
        customerService.update(customerId, customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + customer.getCustomerId());
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

    @GetMapping(path = CUSTOMER_PATH)
    public List<CustomerDTO> getAllCustomers() {
        log.info("CustomerController: Getting all customers");
        return customerService.findAll();
    }

    @GetMapping(path = CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.info("CustomerController: Getting customer by id: " + customerId);
        return customerService.findById(customerId).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping(path = CUSTOMER_PATH_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        log.info("CustomerController: Deleting customer by id: " + customerId);
        customerService.deleteById(customerId);
    }

    @PatchMapping(path = CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        log.info("CustomerController: Patching customer: " + customer);
        customerService.patchCustomer(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}

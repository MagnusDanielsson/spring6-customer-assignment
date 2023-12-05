package edu.springframework.spring6customerassignment.service;

import edu.springframework.spring6customerassignment.exception.NotFoundException;
import edu.springframework.spring6customerassignment.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService{

    private Map<UUID, Customer> customers = new HashMap<>();

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
                .customerName("John Doe")
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("Donald Trump")
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .customerName("Magnus Danielsson")
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(customer1.getCustomerId(), customer1);
        customers.put(customer2.getCustomerId(), customer2);

        log.info("CustomerService: customers: " + customers);
    }

    @Override
    public List<Customer> findAll() {
        log.info("CustomerService: Getting all customers");
        return new ArrayList<>(customers.values());
    }

    @Override
    public Customer findById(UUID id) {
        log.info("CustomerService: Getting customer by id: " + id);
        return customers.get(id);
    }

    @Override
    public Customer save(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .customerName(customer.getCustomerName())
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(savedCustomer.getCustomerId(), savedCustomer);
        log.info("CustomerService: Saving customer: " + savedCustomer);

        return savedCustomer;

    }

    @Override
    public void update(UUID uuid, Customer customer) {
        Customer existing = customers.get(uuid);
        existing.setCustomerName(customer.getCustomerName());
        existing.setLastModifiedDate(LocalDateTime.now());
        log.info("CustomerService: Updating customer: " + existing);
    }

    @Override
    public void deleteById(UUID uuid) {
        Customer customer = customers.remove(uuid);
        log.info("CustomerService: Deleting customer: " + customer);
    }

    @Override
    public void patchCustomer(UUID customerId, Customer customer) {
        Customer existing = customers.get(customerId);
        if(StringUtils.hasText(customer.getCustomerName())){
           existing.setCustomerName(customer.getCustomerName());
           existing.setLastModifiedDate(LocalDateTime.now());
        }

    }

}

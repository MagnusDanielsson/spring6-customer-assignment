package edu.springframework.spring6customerassignment.service;

import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.exception.NotFoundException;
import edu.springframework.spring6customerassignment.mappers.CustomerMapper;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService{

    HashMap<UUID, CustomerDTO> customers = new HashMap<>();

    public CustomerServiceImpl() {
        CustomerDTO customer =
                CustomerDTO.builder().
                        customerId(UUID.randomUUID()).
                        version(1).
                        createdDate(LocalDateTime.now()).
                        lastModifiedDate(LocalDateTime.now()).
                        customerName("Magnus Danielsson").
                        build();

        CustomerDTO customer2 =
                CustomerDTO.builder().
                        customerId(UUID.randomUUID()).
                        version(1).
                        createdDate(LocalDateTime.now()).
                        lastModifiedDate(LocalDateTime.now()).
                        customerName("Lukas Danielsson").
                        build();

        CustomerDTO customer3 =
                CustomerDTO.builder().
                        customerId(UUID.randomUUID()).
                        version(1).
                        createdDate(LocalDateTime.now()).
                        lastModifiedDate(LocalDateTime.now()).
                        customerName("Abel Danielsson").
                        build();


        customers.put(customer.getCustomerId(), customer);
        customers.put(customer2.getCustomerId(), customer2);
        customers.put(customer3.getCustomerId(), customer3);
        customers.forEach((id,c) -> log.info("Id: " + id + ", name :" + c.getCustomerName()));
    }

    @Override
    public List<CustomerDTO> findAll() {
       return new ArrayList<>(customers.values());
    }

    @Override
    public Optional<CustomerDTO> findById(UUID id) {
        return Optional.ofNullable(customers.get(id));
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDto) {
        CustomerDTO newCustomer = CustomerDTO.builder()
                .customerName(customerDto.getCustomerName())
                .customerId(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        log.info("CustomerService: Saving customer: " + newCustomer);
        return customers.put(newCustomer.getCustomerId(), newCustomer);
    }

    @Override
    public Optional<CustomerDTO> update(UUID uuid, CustomerDTO customerDto) {
        Optional<CustomerDTO> existing = Optional.ofNullable(customers.get(uuid));
        existing.ifPresent(existingCustomer -> {
            existingCustomer.setCustomerName(customerDto.getCustomerName());
            existingCustomer.setLastModifiedDate(LocalDateTime.now());
            log.info("CustomerServiceImpl: Updating customer: " + existingCustomer);
        });

        return existing;
    }
    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customerDto) {
        Optional<CustomerDTO> existing = Optional.ofNullable(customers.get(customerId));
        existing.ifPresent(existingCustomer -> {
            if(StringUtils.hasText(customerDto.getCustomerName())) {
                existingCustomer.setCustomerName(customerDto.getCustomerName());
                existingCustomer.setLastModifiedDate(LocalDateTime.now());
                log.info("CustomerServiceImpl: Patching customer: " + existingCustomer);
            }
        });
        return existing;
    }
    @Override
    public Boolean deleteById(UUID customerId) {
        customers.remove(customerId);
        return Boolean.TRUE;
    }
}

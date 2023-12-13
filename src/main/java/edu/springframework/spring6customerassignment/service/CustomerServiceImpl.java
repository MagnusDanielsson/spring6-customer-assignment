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
        CustomerDTO customer = customers.get(id);
        return Optional.ofNullable(customer);
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
        existing.ifPresent(foundCustomer -> {
            foundCustomer.setCustomerName(customerDto.getCustomerName());
            foundCustomer.setLastModifiedDate(LocalDateTime.now());
            log.info("CustomerServiceImpl: Updating customer: " + foundCustomer);
        });

        return existing;
    }

    @Override
    public void deleteById(UUID uuid) {
        CustomerDTO customer = customers.remove(uuid);
        log.info("CustomerServiceImpl: Deleting customer: " + customer);
    }

    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customerDto) {
        Optional<CustomerDTO> existing = Optional.ofNullable(customers.get(customerId));
        existing.ifPresent(foundCustomer -> {
            if(StringUtils.hasText(customerDto.getCustomerName())) {
                foundCustomer.setCustomerName(customerDto.getCustomerName());
                foundCustomer.setLastModifiedDate(LocalDateTime.now());
            }
        });
        return existing;
    }

}

package edu.springframework.spring6customerassignment.service;

import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.exception.NotFoundException;
import edu.springframework.spring6customerassignment.mappers.CustomerMapper;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import edu.springframework.spring6customerassignment.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
@Primary
public class CustomerServiceJPA implements CustomerService{

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> findAll() {
        log.info("CustomerServiceJPA: Getting all customers");
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).toList();
    }

    @Override
    public Optional<CustomerDTO> findById(UUID id) {
        log.info("CustomerServiceJPA: Getting customer by id: " + id);
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        Optional<Customer> customer = customerRepository.findById(id);

        customer.ifPresentOrElse(foundCustomer -> {
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(foundCustomer)));
        }, () -> {atomicReference.set(Optional.empty());});

        return atomicReference.get();
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDto) {
        CustomerDTO newCustomerDto = CustomerDTO.builder()
                .customerName(customerDto.getCustomerName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer persistedCustomer = customerRepository.save(customerMapper.customerDtoToCustomer(newCustomerDto));
        log.info("CustomerServiceJPA: Persisted customer: " + persistedCustomer);
        return customerMapper.customerToCustomerDto(persistedCustomer);
    }

    @Override
    public Optional<CustomerDTO> update(UUID customerId, CustomerDTO customerDto) {
        AtomicReference<Optional<CustomerDTO>> response = new AtomicReference<>();
        Optional<Customer> existing = customerRepository.findById(customerId);

        // Check if present and update
        existing.ifPresentOrElse(foundCustomer -> {
            log.info("CustomerServiceJPA: Updating customer: " + foundCustomer);

            foundCustomer.setCustomerName(customerDto.getCustomerName());
            foundCustomer.setLastModifiedDate(LocalDateTime.now());

            Customer persistedCustomer = customerRepository.save(foundCustomer);
            response.set(Optional.of(customerMapper.customerToCustomerDto(persistedCustomer)));
        }, () -> { response.set(Optional.empty());});

        return response.get();
    }

    @Override
    public Boolean deleteById(UUID uuid) {
        log.info("CustomerServiceJPA: Deleting customer");
        if(customerRepository.existsById(uuid)) {
            customerRepository.deleteById(uuid);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> response = new AtomicReference<>();
        Optional<Customer> existing = customerRepository.findById(customerId);

        // Check if present and patch
        existing.ifPresentOrElse(foundCustomer -> {
            log.info("CustomerServiceJPA: Patch customer " + foundCustomer);

            foundCustomer.setCustomerName(customer.getCustomerName());
            foundCustomer.setLastModifiedDate(LocalDateTime.now());

            customerRepository.save(foundCustomer);
            response.set(Optional.of(customerMapper.customerToCustomerDto(foundCustomer)));
        }, () -> {
            response.set(Optional.empty());
        });

        return response.get();
    }
}

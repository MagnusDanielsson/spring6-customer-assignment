package edu.springframework.spring6customerassignment.service;

import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.mappers.CustomerMapper;
import edu.springframework.spring6customerassignment.mappers.CustomerMapperImpl;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import edu.springframework.spring6customerassignment.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
//@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService{
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;

        Customer customer1 = Customer.builder()
                .customerName("John Doe")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("Donald Trump")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .customerName("Magnus Danielsson")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer4 = Customer.builder()
                .customerName("The King")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customer1= this.customerRepository.save(customer1);
        log.info("Customer entity #1 " + customer1.getCustomerName());

        customer2= this.customerRepository.save(customer2);
        log.info("Customer entity #2 " + customer2.getCustomerName());

        customer3= this.customerRepository.save(customer3);
        log.info("Customer entity #3 " + customer3.getCustomerName());

        customer4= this.customerRepository.save(customer4);
        log.info("Customer entity #4 " + customer4.getCustomerName());

        List<Customer> customerList = customerRepository.findAll();
        customerList.forEach(c -> System.out.println("Customer name -> " + c.toString()));
    }

    @Override
    public List<CustomerDTO> findAll() {
        log.info("CustomerService: Getting all customers");
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for (Customer c : customerList) {
            customerDTOList.add(customerMapper.customerToCustomerDto(c));
        }
        return customerDTOList;
    }

    @Override
    public Optional<CustomerDTO> findById(UUID id) {
        log.info("CustomerService: Getting customer by id: " + id);
        Optional<Customer> customer = customerRepository.findById(id);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer.orElse(null));
        return Optional.ofNullable(customerDTO);
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDto) {
        CustomerDTO newCustomerDto = CustomerDTO.builder()
                .customerName(customerDto.getCustomerName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer = customerMapper.customerDtoToCustomer(newCustomerDto);

        Customer persistedCustomer = customerRepository.save(customer);
        log.info("CustomerService: Persisted customer: " + persistedCustomer);

        newCustomerDto = customerMapper.customerToCustomerDto(persistedCustomer);
        return newCustomerDto;
    }

    @Override
    public void update(UUID uuid, CustomerDTO customerDto) {
        Optional<Customer> existing = customerRepository.findById(uuid);
        Customer existingCustomer = existing.get();
        existingCustomer.setCustomerName(customerDto.getCustomerName());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        existingCustomer = customerRepository.save(existingCustomer);
        log.info("CustomerService: Updating customer: " + existingCustomer);
    }

    @Override
    public void deleteById(UUID uuid) {
        customerRepository.deleteById(uuid);
        log.info("CustomerService: Deleting customer");
    }

    @Override
    public void patchCustomer(UUID customerId, CustomerDTO customer) {
        Optional<Customer> existing = customerRepository.findById(customerId);
        Customer existingCustomer = existing.get();

        if(StringUtils.hasText(customer.getCustomerName())){
           existingCustomer.setCustomerName(customer.getCustomerName());
           existingCustomer.setLastModifiedDate(LocalDateTime.now());
           existingCustomer = customerRepository.save(existingCustomer);
           log.info("Updated customer: " + existingCustomer);
        }

    }

}

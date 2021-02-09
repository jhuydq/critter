package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer findCustomerById(Long id){
        return customerRepository.findById(id).get();
    }

    public List<Customer> findAllCustomers(){
        return customerRepository.findAll();
    }

    public void addPetToCustomer(Pet pet){
        if(pet.getOwner() != null){
            Customer customer = pet.getOwner();
            List<Pet> petList = customer.getPetList();
            if(petList == null){
                petList = new ArrayList<>();
            }
            petList.add(pet);
            customer.setPetList(petList);
            customerRepository.save(customer);
        }
    }
}

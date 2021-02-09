package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repo.CustomerRepository;
import com.udacity.jdnd.course3.critter.repo.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    ScheduleService scheduleService;

    public Pet savePet(Pet pet){
        Pet savedPet = petRepository.save(pet);

        //sync Pet with its Owner
        if(savedPet.getOwner() != null){
            customerService.addPetToCustomer(savedPet);
        }
        //sync Pet with Schedule
        if(savedPet.getSchedule() != null){
            scheduleService.addPetToSchedule(savedPet);
        }
        return savedPet;
    }

//    @Autowired
//    CustomerRepository customerRepository;
//
//    public Pet savePet(Pet pet){
//        Pet savedPet =  petRepository.save(pet);
//        Customer customer = savedPet.getOwner();
//        List<Pet> customerPets = customer.getPetList();
//        if(customerPets == null){
//            customerPets = new ArrayList<>();
//        }
//        customerPets.add(savedPet);
//        customer.setPetList(customerPets);
//        customerRepository.save(customer);
//        return savedPet;
//    }

    public Pet findPetById(Long id) throws PetExceptionNotFound {
        Pet pet = null;
        try {
            pet = petRepository.findById(id).get();
        }catch (Exception e){
            throw new PetExceptionNotFound("Pet not found by id "+id);
        }

        return pet;
    }

    public List<Pet> findAllPets(){
        return petRepository.findAll();
    }

    public List<Pet> findPetsByOwnerId(Long ownerId){
        //Way 1
//        List<Pet> petList = new ArrayList<>();
//        petRepository.findAll().forEach(pet -> {
//            if(pet.getOwner().getId().equals(ownerId)){
//                petList.add(pet);
//            }
//        });
//        return petList;

        //Way 2
//        Customer owner = customerService.findCustomerById(ownerId);
//        return petRepository.findPetsByOwner(owner);

        //Way 3
        return petRepository.findPetsByOwnerId(ownerId);
    }

    public Customer findOwnerByPetId(Long petId){
        return petRepository.findOwnerByPetId(petId);
    }
}

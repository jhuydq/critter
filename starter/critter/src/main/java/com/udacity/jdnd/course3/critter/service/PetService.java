package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repo.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;

    public Pet savePet(Pet pet){
        return petRepository.save(pet);
    }

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
        List<Pet> petList = new ArrayList<>();
        petRepository.findAll().forEach(pet -> {
            if(pet.getOwner().getId().equals(ownerId)){
                petList.add(pet);
            }
        });
        return petList;
    }

}

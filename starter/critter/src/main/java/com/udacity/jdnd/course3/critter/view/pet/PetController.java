package com.udacity.jdnd.course3.critter.view.pet;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetExceptionNotFound;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return convertToPetDTO(petService.savePet(convertToPet(petDTO)));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        PetDTO petDTO = null;
        try {
            petDTO = convertToPetDTO(petService.findPetById(petId));
        }catch (PetExceptionNotFound e){
            e.printStackTrace();
        }
        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> petDTOList = new ArrayList<>();
        petService.findAllPets().forEach(pet -> {
            petDTOList.add(convertToPetDTO(pet));
        });
        return petDTOList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> petDTOList = new ArrayList<>();
        petService.findPetsByOwnerId(ownerId).forEach(pet -> {
            petDTOList.add(convertToPetDTO(pet));
        });
        return petDTOList;
    }

    private PetDTO convertToPetDTO(Pet pet){
        PetDTO petDTO =new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if(pet.getOwner() != null){
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }

    private Pet convertToPet(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        try {
            pet.setOwner(customerService.findCustomerById(petDTO.getOwnerId()));
        }catch (Exception e){
            if(e instanceof NoSuchElementException){
                System.out.println("cannot find this pet's owner");
            }
            pet.setOwner(null);
        }
        return pet;
    }
}

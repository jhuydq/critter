package com.udacity.jdnd.course3.critter.repo;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p WHERE p.owner.id = :ownerId")
    public List<Pet> findPetsByOwnerId(Long ownerId);

    @Query("SELECT p FROM Pet p WHERE p.owner = :owner")
    public List<Pet> findPetsByOwner(Customer owner);

    @Query("SELECT p.owner FROM Pet p WHERE p.id = :petId")
    public Customer findOwnerByPetId(Long petId);
}

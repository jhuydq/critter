package com.udacity.jdnd.course3.critter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @Nationalized
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> petList;

//    public boolean addPet(Pet pet){
//        if(petList == null){
//            petList = new ArrayList<>();
//        }
//        if(!petList.contains(pet)){
//            petList.add(pet);
//            pet.setOwner(this);
//            return true;
//        }
//        return false;
//    }
//
//    public void removePet(Pet pet){
//        if(petList != null && petList.contains(pet)){
//            petList.remove(pet);
//            pet.setOwner(null);
//        }
//    }


}

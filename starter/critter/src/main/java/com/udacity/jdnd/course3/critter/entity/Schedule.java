package com.udacity.jdnd.course3.critter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> activities;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Employee> employeeList;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Pet> petList;
}

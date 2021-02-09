package com.udacity.jdnd.course3.critter.repo;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE :date MEMBER OF e.daysAvailable")
    public List<Employee> findEmployeesForAvailableDay(DayOfWeek date);
//
//    @Query("SELECT e FROM Employee e JOIN e.skills s WHERE :date MEMBER OF e.daysAvailable")
//    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, DayOfWeek date);
}

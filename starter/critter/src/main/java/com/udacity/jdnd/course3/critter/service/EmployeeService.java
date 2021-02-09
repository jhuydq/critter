package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ScheduleService scheduleService;

    public Employee saveEmployee(Employee employee){
        Employee savedEmployee = employeeRepository.save(employee);
        //sync Employee with Schedule
        if(savedEmployee.getSchedule() != null){
            scheduleService.addEmployeeToSchedule(savedEmployee);
        }
        return savedEmployee;
    }

    public Employee findEmployeeById(Long id){
        return employeeRepository.findById(id).get();
    }

    public Employee setAvailability(Set<DayOfWeek> daysAvailable, Long id) throws EmployeeExceptionNotFound {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            Employee employee = employeeRepository.findById(id).get();
            employee.setDaysAvailable(daysAvailable);
            return employeeRepository.save(employee);
        }else{
            throw new EmployeeExceptionNotFound("Employee , id = "+id+ " not found");
        }
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, DayOfWeek date){
        List<Employee> employeeList = employeeRepository.findEmployeesForAvailableDay(date);
        List<Employee> employeeList2 = new ArrayList<>();
        employeeList.forEach(employee -> {
            if(employee.getSkills().containsAll(skills)){
                employeeList2.add(employee);
            }
        });
        return employeeList2;

//        return employeeRepository.findEmployeesForService(skills, date);
    }
}

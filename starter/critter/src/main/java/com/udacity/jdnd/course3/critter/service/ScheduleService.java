package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repo.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAllSchedules(){
        return scheduleRepository.findAll();
    }

    //FIXME: this one is wrong
    public List<Schedule> findScheduleForEmployee(Employee employee){
        List<Schedule> scheduleList = scheduleRepository.findScheduleForEmployee(employee);
        System.out.println("Employee : id "+employee.getId() + ", skills = "+employee.getSkills() + ", DaysAvailable = "+employee.getDaysAvailable());
        System.out.println("size "+scheduleList.size());
        return scheduleList;
    }

    public List<Schedule> findScheduleForEmployee(Long employeeId){
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleRepository.findAll().forEach(schedule -> {
            for(int i = 0; i < schedule.getEmployeeList().size(); i++){
                Employee tmpEmployee = schedule.getEmployeeList().get(i);
                if(tmpEmployee.getId() == employeeId){
                    scheduleList.add(schedule);
                    break;
                }
            }
        });

        return scheduleList;
    }

    public List<Schedule> findScheduleForPet(Long petId){
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleRepository.findAll().forEach(schedule -> {
            for(int i = 0; i < schedule.getPetList().size(); i++){
                Pet tmpPet = schedule.getPetList().get(i);
                if(tmpPet.getId() == petId){
                    scheduleList.add(schedule);
                    break;
                }
            }
        });

        return scheduleList;
    }

    public List<Schedule> findScheduleForCustomer(Long customerId){
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleRepository.findAll().forEach(schedule -> {
            for(int i = 0; i < schedule.getPetList().size(); i++){
                Pet tmpPet = schedule.getPetList().get(i);

                if(tmpPet.getOwner().getId() == customerId && !scheduleList.contains(schedule)){
                    scheduleList.add(schedule);
                    break;
                }
            }
        });

        return scheduleList;
    }

    public void addEmployeeToSchedule(Employee employee){
        if(employee.getSchedule() != null){
            Schedule schedule = employee.getSchedule();
            List<Employee> employeeList = schedule.getEmployeeList();
            if(employeeList == null){
                employeeList = new ArrayList<>();
            }
            employeeList.add(employee);
            schedule.setEmployeeList(employeeList);
            scheduleRepository.save(schedule);
        }
    }

    public void addPetToSchedule(Pet pet){
        if(pet.getSchedule() != null){
            Schedule schedule = pet.getSchedule();
            List<Pet> petList = schedule.getPetList();
            if(petList == null){
                petList = new ArrayList<>();
            }
            petList.add(pet);
            schedule.setPetList(petList);
            scheduleRepository.save(schedule);
        }
    }
}

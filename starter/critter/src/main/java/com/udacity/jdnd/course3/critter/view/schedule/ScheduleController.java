package com.udacity.jdnd.course3.critter.view.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    PetService petService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertToScheduleDTO(scheduleService.saveSchedule(convertToSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleService.findAllSchedules().forEach(schedule -> {
            scheduleDTOList.add(convertToScheduleDTO(schedule));
        });
       return scheduleDTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleService.findScheduleForPet(petId).forEach(schedule -> {
            scheduleDTOList.add(convertToScheduleDTO(schedule));
        });

        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        scheduleService.findScheduleForEmployee(employeeId).forEach(schedule -> {
            scheduleDTOList.add(convertToScheduleDTO(schedule));
        });
        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

        scheduleService.findScheduleForCustomer(customerId).forEach(schedule -> {
            scheduleDTOList.add(convertToScheduleDTO(schedule));
        });
        return scheduleDTOList;
    }

    private Schedule convertToSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        //petIds -> petList
        List<Pet> petList = new ArrayList<>();
        try{
            scheduleDTO.getPetIds().forEach(petId ->{
                try {
                    petList.add(petService.findPetById(petId));
                } catch (PetExceptionNotFound petExceptionNotFound) {
                    petExceptionNotFound.printStackTrace();
                }
            });
            schedule.setPetList(petList);
        }catch (Exception e){
            System.out.println("petIds null");
        }

        //employeeIds -> employeeList
        List<Employee> employeeList = new ArrayList<>();
        try{
            scheduleDTO.getEmployeeIds().forEach(employeeId -> {
                try {
                    employeeList.add(employeeService.findEmployeeById(employeeId));
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
            schedule.setEmployeeList(employeeList);
        }catch (Exception e){
            System.out.println("employeeIds null");
        }

        return schedule;
    }

    private ScheduleDTO convertToScheduleDTO(Schedule schedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        List<Long> petIds = new ArrayList<>();
        if(schedule.getPetList() != null){
            schedule.getPetList().forEach(pet -> {
                petIds.add(pet.getId());
            });
            scheduleDTO.setPetIds(petIds);
        }

        List<Long> employeeIds = new ArrayList<>();
        if(schedule.getEmployeeList() != null){
            schedule.getEmployeeList().forEach(employee -> {
                employeeIds.add(employee.getId());
            });
            scheduleDTO.setEmployeeIds(employeeIds);
        }
        return scheduleDTO;
    }
}

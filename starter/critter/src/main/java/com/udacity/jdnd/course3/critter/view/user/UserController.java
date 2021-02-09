package com.udacity.jdnd.course3.critter.view.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;

    @Autowired
    PetService petService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return convertToCustomerDTO(customerService.saveCustomer(convertToCustomer(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerService.findAllCustomers().forEach(customer -> {
            customerDTOList.add(convertToCustomerDTO(customer));
        });
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return convertToCustomerDTO(petService.findOwnerByPetId(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return convertToEmployeeDTO(employeeService.saveEmployee(convertToEmployee(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertToEmployeeDTO(employeeService.findEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try{
            Employee employee = employeeService.setAvailability(daysAvailable, employeeId);
        }catch (EmployeeExceptionNotFound e){
            e.printStackTrace();
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        List<Employee> employeeList = employeeService.findEmployeesForService(employeeDTO.getSkills(), employeeDTO.getDate().getDayOfWeek());
        employeeList.forEach(employee -> {
            employeeDTOList.add(convertToEmployeeDTO(employee));
        });

        return employeeDTOList;
    }

    private CustomerDTO convertToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Long> petIds = new ArrayList<>();
        try {
            customer.getPetList().forEach(pet -> {
                petIds.add(pet.getId());
            });
            customerDTO.setPetIds(petIds);
        }catch (Exception e){
            System.out.println("customer "+customer.getId() + " does not have any pet");
        }
        return customerDTO;
    }

    private Customer convertToCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        //petIds -> petList
        List<Pet> petList = new ArrayList<>();
        try{
            customerDTO.getPetIds().forEach(petId ->{
                try {
                    petList.add(petService.findPetById(petId));
                } catch (PetExceptionNotFound petExceptionNotFound) {
                    petExceptionNotFound.printStackTrace();
                }
            });
            customer.setPetList(petList);
        }catch (Exception e){
            System.out.println("petIds null");
        }
        return customer;
    }

    private Employee convertToEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

}

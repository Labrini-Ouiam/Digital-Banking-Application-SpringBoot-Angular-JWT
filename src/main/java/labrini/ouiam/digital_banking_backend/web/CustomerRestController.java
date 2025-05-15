//package labrini.ouiam.digital_banking_backend.web;
//
//import labrini.ouiam.digital_banking_backend.Exception.CustomerNotFoundException;
//import labrini.ouiam.digital_banking_backend.dtos.CustomerDTO;
//import labrini.ouiam.digital_banking_backend.entities.BankAccount;
//import labrini.ouiam.digital_banking_backend.entities.Customer;
//import labrini.ouiam.digital_banking_backend.services.BankAccountService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//
//public class CustomerRestController {
//    private BankAccountService bankAccountService;
//
//    @GetMapping("/customers")
//    public List<CustomerDTO> customers() {
//        return bankAccountService.listCustomers();
//    }
//
//    @GetMapping("/customers/{id}")
//    public CustomerDTO getCustomer(@PathVariable Long id) throws CustomerNotFoundException {
//        return bankAccountService.getCustomerDTOById(id);
//    }
//
//    @PostMapping("/customers")
//    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
//        return bankAccountService.SaveCustomer(customerDTO);
//    }
//
//    @PutMapping("/customers/{id}")
//    public CustomerDTO updateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
//        customerDTO.setId(id);
//        return bankAccountService.UpdateCustomer(customerDTO);
//    }
//
//    @DeleteMapping("/customers/{id}")
//    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
//        bankAccountService.deleteCustomer(id);
//    }
//}
package labrini.ouiam.digital_banking_backend.web;

import labrini.ouiam.digital_banking_backend.Exception.CustomerNotFoundException;
import labrini.ouiam.digital_banking_backend.dtos.CustomerDTO;
import labrini.ouiam.digital_banking_backend.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
//@CrossOrigin(origins = "*")
@RequestMapping("/customers")
public class CustomerRestController {
    private final BankAccountService bankAccountService;

    public CustomerRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public List<CustomerDTO> customers() {
        return bankAccountService.listCustomers();
    }
    @GetMapping("/search")
    public List<CustomerDTO> SearchCustomers(@RequestParam(name="keyword",defaultValue = "")String keyword) {
        return bankAccountService.searchCustemers("%"+keyword+"%");
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomerDTOById(id);
    }

    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.SaveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(id);
        return bankAccountService.UpdateCustomer(customerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        bankAccountService.deleteCustomer(id);
    }
}
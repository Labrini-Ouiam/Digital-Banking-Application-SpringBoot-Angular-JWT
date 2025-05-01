package labrini.ouiam.digital_banking_backend.repositories;

import labrini.ouiam.digital_banking_backend.entities.BankAccount;
import labrini.ouiam.digital_banking_backend.entities.Customer;
import labrini.ouiam.digital_banking_backend.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
//    List<BankAccount> findByCustomer(Customer customer);
//    List<BankAccount> findByCustomerAndOperationType(Customer customer, OperationType operationType);
//    List<BankAccount> findByCustomerAndOperationTypeAndDateBetween(Customer customer, OperationType operationType, LocalDate startDate, LocalDate endDate);
//    List<BankAccount> findByCustomerAndDateBetween(Customer customer, LocalDate startDate, LocalDate endDate);
//    List<BankAccount> findByOperationType(OperationType operationType);
//    List<BankAccount> findByOperationTypeAndDateBetween(OperationType operationType, LocalDate startDate, LocalDate endDate);
}

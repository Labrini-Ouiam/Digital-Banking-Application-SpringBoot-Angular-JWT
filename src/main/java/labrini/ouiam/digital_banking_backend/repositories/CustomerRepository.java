package labrini.ouiam.digital_banking_backend.repositories;

import labrini.ouiam.digital_banking_backend.entities.Customer;
import labrini.ouiam.digital_banking_backend.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
//    List<Customer> findByAccountOperations_OperationType(OperationType operationType);
//    List<Customer> findByAccountOperations_OperationDateBetween(LocalDate startDate, LocalDate endDate);
//    List<Customer> findByAccountOperations_OperationAmountGreaterThan(Double amount);
//    List<Customer> findByAccountOperations_OperationDescriptionContaining(String description);
//    List<Customer> findByAccountOperations_OperationTypeAndOperationDateBetween(OperationType operationType, LocalDate startDate, LocalDate endDate);
}

package labrini.ouiam.digital_banking_backend.repositories;

import labrini.ouiam.digital_banking_backend.entities.AccountOperation;
import labrini.ouiam.digital_banking_backend.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
//    List<AccountOperation> findByAccountIdAndOperationDateBetween(Long accountId, LocalDate startDate, LocalDate endDate);
//    List<AccountOperation> findByAccountIdAndOperationType(Long accountId, OperationType operationType);
//    List<AccountOperation> findByAccountIdAndAmountGreaterThanEqual(Long accountId, Double amount);
//    List<AccountOperation> findByAccountIdAndAmountLessThanEqual(Long accountId, Double amount);
//    List<AccountOperation> findByAccountIdAndDescriptionContainingIgnoreCase(Long accountId, String description);
}

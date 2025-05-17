package labrini.ouiam.digital_banking_backend.repositories;

import labrini.ouiam.digital_banking_backend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}

package labrini.ouiam.digital_banking_backend;

import labrini.ouiam.digital_banking_backend.Exception.BalanceNotSufficientException;
import labrini.ouiam.digital_banking_backend.Exception.BankAccountNotFoundException;
import labrini.ouiam.digital_banking_backend.Exception.CustomerNotFoundException;
import labrini.ouiam.digital_banking_backend.entities.*;
import labrini.ouiam.digital_banking_backend.enums.OperationType;
import labrini.ouiam.digital_banking_backend.repositories.AccountOperationRepository;
import labrini.ouiam.digital_banking_backend.repositories.BankAccountRepository;
import labrini.ouiam.digital_banking_backend.repositories.CustomerRepository;
import labrini.ouiam.digital_banking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import labrini.ouiam.digital_banking_backend.enums.AccountStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("ouiam","Mohammed","ouissal").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.SaveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(cust -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, cust.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 90000, 5.5, cust.getId());
                    List<BankAccount> bankAccounts= bankAccountService.bankAccountList();
                    for (BankAccount bankAccount : bankAccounts) {
                        for (int i = 0; i < 10; i++) {
                            bankAccountService.Debit(bankAccount.getId(), 1000 + Math.random() * 1200, "Debit");
                            bankAccountService.Credit(bankAccount.getId(), 1000 + Math.random() * 1200, "Credit");
                        }
                    }
                } catch (CustomerNotFoundException | BankAccountNotFoundException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            });
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreateDate(new Date());
                currentAccount.setStatus(AccountStatus.Created);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreateDate(new Date());
                savingAccount.setStatus(AccountStatus.Created);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(bac -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 1200);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(bac);
                    accountOperationRepository.save(accountOperation);
                }
            });

        };
    }


}

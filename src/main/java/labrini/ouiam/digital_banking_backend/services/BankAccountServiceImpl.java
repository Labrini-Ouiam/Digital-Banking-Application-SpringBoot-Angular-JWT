package labrini.ouiam.digital_banking_backend.services;

import jakarta.transaction.Transactional;
import labrini.ouiam.digital_banking_backend.Exception.BalanceNotSufficientException;
import labrini.ouiam.digital_banking_backend.Exception.BankAccountNotFoundException;
import labrini.ouiam.digital_banking_backend.Exception.CustomerNotFoundException;
import labrini.ouiam.digital_banking_backend.entities.*;
import labrini.ouiam.digital_banking_backend.enums.OperationType;
import labrini.ouiam.digital_banking_backend.repositories.AccountOperationRepository;
import labrini.ouiam.digital_banking_backend.repositories.BankAccountRepository;
import labrini.ouiam.digital_banking_backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import labrini.ouiam.digital_banking_backend.enums.AccountStatus;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
//pour la journalisation
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    // injecrtion par constructeur
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;

    @Override
    public Customer SaveCustomer(Customer customer) {
        log.info("Saving new customer {} to the database", customer.getName());
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        log.info("Saving new Current Bank Account for customer with ID {}", customerId);
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CurrentAccount bankAccount = new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCreateDate(new java.util.Date());
        bankAccount.setStatus(AccountStatus.Created);
        bankAccount.setCustomer(customer);
        bankAccount.setOverDraft(overDraft);
        CurrentAccount account= bankAccountRepository.save(bankAccount);
        return account;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        log.info("Saving new Saving Bank Account for customer with ID {}", customerId);
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        SavingAccount bankAccount = new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCreateDate(new java.util.Date());
        bankAccount.setStatus(AccountStatus.Created);
        bankAccount.setCustomer(customer);
        bankAccount.setInterestRate(interestRate);
        SavingAccount account= bankAccountRepository.save(bankAccount);
        return account;
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String AccountId) throws BankAccountNotFoundException {
        log.info("Fetching bank account with ID {}", AccountId);
        BankAccount bankAccount = bankAccountRepository.findById(AccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        return bankAccount;
    }

    @Override
    public void Debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=getBankAccount(accountId);
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Insufficient funds");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new java.util.Date());
        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void Credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationDate(new java.util.Date());
        accountOperation.setAmount(amount);
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void Transfer(String accountIdSource, String accountIdDestination, double amount) {
        try {
            Debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
            Credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
        } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
            log.error("Error during transfer: {}", e.getMessage());
        }

    }
    @Override
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }
}

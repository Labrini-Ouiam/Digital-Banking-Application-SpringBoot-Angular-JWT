package labrini.ouiam.digital_banking_backend.services;

import jakarta.transaction.Transactional;
import labrini.ouiam.digital_banking_backend.Exception.BalanceNotSufficientException;
import labrini.ouiam.digital_banking_backend.Exception.BankAccountNotFoundException;
import labrini.ouiam.digital_banking_backend.Exception.CustomerNotFoundException;
import labrini.ouiam.digital_banking_backend.dtos.*;
import labrini.ouiam.digital_banking_backend.entities.*;
import labrini.ouiam.digital_banking_backend.enums.OperationType;
import labrini.ouiam.digital_banking_backend.mappers.BankAccountMapperImpl;
import labrini.ouiam.digital_banking_backend.repositories.AccountOperationRepository;
import labrini.ouiam.digital_banking_backend.repositories.BankAccountRepository;
import labrini.ouiam.digital_banking_backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import labrini.ouiam.digital_banking_backend.enums.AccountStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public CustomerDTO SaveCustomer(CustomerDTO customer) {
        log.info("Saving new customer with name {}", customer.getName());
        Customer customerEntity = bankAccountMapper.FromCustomerDTO(customer);
        Customer savedCustomer = customerRepository.save(customerEntity);
        return bankAccountMapper.FromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
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
        return bankAccountMapper.FromCurrentBankAccount(account);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
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
        return bankAccountMapper.FromSavingBankAccount(account);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers= customerRepository.findAll();
        List<CustomerDTO> customerDTOS= customers.stream().map(cust -> bankAccountMapper.FromCustomer(cust)).toList();
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String AccountId) throws BankAccountNotFoundException {
        log.info("Fetching bank account with ID {}", AccountId);
        BankAccount bankAccount = bankAccountRepository.findById(AccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount instanceof SavingAccount) {
            return bankAccountMapper.FromSavingBankAccount((SavingAccount) bankAccount);
        } else if (bankAccount instanceof CurrentAccount) {
            return bankAccountMapper.FromCurrentBankAccount((CurrentAccount) bankAccount);
        }
        return null;
    }

    @Override
    public void Debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
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
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
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
    public List<BankAccountDTO> bankAccountList() {
        log.info("Fetching all bank accounts");
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                return bankAccountMapper.FromSavingBankAccount((SavingAccount) bankAccount);
            } else {
                return bankAccountMapper.FromCurrentBankAccount((CurrentAccount) bankAccount);
            }
        }).toList();
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomerDTOById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.FromCustomer(customer);
    }

    @Override
    public CustomerDTO UpdateCustomer(CustomerDTO customer) {
        log.info("Updating customer with ID {}", customer.getId());
        Customer customerEntity = bankAccountMapper.FromCustomerDTO(customer);
        Customer savedCustomer = customerRepository.save(customerEntity);
        return bankAccountMapper.FromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        log.info("Deleting customer with ID {}", id);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public List<AccountOperationDTO> AccountOperationsHistory(String accountId){
        List<AccountOperation> operations = accountOperationRepository.findByBankAccountId(accountId);
        return operations.stream().map(operation -> bankAccountMapper.FromAccountOperation(operation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        Page<AccountOperation> operations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> operationDTOS = operations.getContent().stream()
                .map(operation -> bankAccountMapper.FromAccountOperation(operation))
                .collect(Collectors.toList());
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setTotalPages(operations.getTotalPages());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setAccountOperationDTO(operationDTOS);
        return accountHistoryDTO;
    }
    @Override
    public List<CustomerDTO> searchCustemers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        return customers.stream().map(cust -> bankAccountMapper.FromCustomer(cust)).collect(Collectors.toList());
    }
}

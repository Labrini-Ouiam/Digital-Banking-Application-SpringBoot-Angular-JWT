package labrini.ouiam.digital_banking_backend.services;

import labrini.ouiam.digital_banking_backend.Exception.BalanceNotSufficientException;
import labrini.ouiam.digital_banking_backend.Exception.BankAccountNotFoundException;
import labrini.ouiam.digital_banking_backend.Exception.CustomerNotFoundException;
import labrini.ouiam.digital_banking_backend.dtos.*;
import labrini.ouiam.digital_banking_backend.entities.BankAccount;
import labrini.ouiam.digital_banking_backend.entities.CurrentAccount;
import labrini.ouiam.digital_banking_backend.entities.Customer;
import labrini.ouiam.digital_banking_backend.entities.SavingAccount;

import java.util.List;

public interface BankAccountService {
    CustomerDTO SaveCustomer(CustomerDTO customer);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double OverDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String AccountId) throws BankAccountNotFoundException;
    void Debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void Credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void Transfer(String accountIdSource, String accountIdDestination, double amount);
    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomerDTOById(Long id) throws CustomerNotFoundException;

    CustomerDTO UpdateCustomer(CustomerDTO customer);

    void deleteCustomer(Long id) throws CustomerNotFoundException;

    List<AccountOperationDTO> AccountOperationsHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}

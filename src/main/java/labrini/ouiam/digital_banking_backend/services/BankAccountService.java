package labrini.ouiam.digital_banking_backend.services;

import labrini.ouiam.digital_banking_backend.Exception.BalanceNotSufficientException;
import labrini.ouiam.digital_banking_backend.Exception.BankAccountNotFoundException;
import labrini.ouiam.digital_banking_backend.Exception.CustomerNotFoundException;
import labrini.ouiam.digital_banking_backend.entities.BankAccount;
import labrini.ouiam.digital_banking_backend.entities.CurrentAccount;
import labrini.ouiam.digital_banking_backend.entities.Customer;
import labrini.ouiam.digital_banking_backend.entities.SavingAccount;

import java.util.List;

public interface BankAccountService {
    Customer SaveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double OverDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String AccountId) throws BankAccountNotFoundException;
    void Debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void Credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void Transfer(String accountIdSource, String accountIdDestination, double amount);
    List<BankAccount> bankAccountList();
}

package labrini.ouiam.digital_banking_backend.dtos;

import jakarta.persistence.*;
import labrini.ouiam.digital_banking_backend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;
@Data
public class CurrentBankAccountDTO extends BankAccountDTO{
    @Id
    private String id;
    private double balance;
    private Date CreateDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}

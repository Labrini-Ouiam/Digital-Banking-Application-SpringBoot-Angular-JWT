package labrini.ouiam.digital_banking_backend.dtos;

import labrini.ouiam.digital_banking_backend.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date OperationDate;
    private Double amount;
    private OperationType type;
    private String description;
}

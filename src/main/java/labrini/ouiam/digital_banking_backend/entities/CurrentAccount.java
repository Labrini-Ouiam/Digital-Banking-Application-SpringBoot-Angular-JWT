package labrini.ouiam.digital_banking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CC")
@Data @AllArgsConstructor @NoArgsConstructor
public class CurrentAccount extends BankAccount {
    private double overDraft;
}

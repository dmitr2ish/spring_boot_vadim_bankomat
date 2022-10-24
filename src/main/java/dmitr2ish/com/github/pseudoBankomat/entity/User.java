package dmitr2ish.com.github.pseudoBankomat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * @author dmitry ishmitov
 * @date 10/22/22
 * TODO description
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "bank_user")
public class User {
    @Id
    private String id;
    private BigDecimal balance;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Operation> operationSet;
}

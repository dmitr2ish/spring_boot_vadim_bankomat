package dmitr2ish.com.github.pseudoBankomat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "operation")
public class Operation {
    @Id
    private String id;
    private LocalDate dateOfOperation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_operation")
    private TypeOfOperation typeOfOperation;

    private BigDecimal amount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

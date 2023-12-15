package edu.springframework.spring6customerassignment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="CUSTOMER")
public class Customer {

    @Id
    //@GeneratedValue(generator = "UUID")
    //@GenericGenerator(name = "UUID")
    //@Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    @Version
    private Integer version;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(length = 50)
    private String customerName;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", version=" + version +
                ", customerName='" + customerName + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}

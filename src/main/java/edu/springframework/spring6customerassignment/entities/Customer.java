package edu.springframework.spring6customerassignment.entities;

import jakarta.persistence.*;
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

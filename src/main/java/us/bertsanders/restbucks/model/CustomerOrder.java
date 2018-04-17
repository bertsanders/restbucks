package us.bertsanders.restbucks.model;

import lombok.Data;
import us.bertsanders.restbucks.model.Payment;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bert on 2/5/17.
 */

@Data
@Entity
public class CustomerOrder {
    public enum Status {
        PAYMENT_EXPECTED,
        PAID,
        PREPARING,
        READY,
        TAKEN,
        CANCELED,
    };

    @Id
    @GeneratedValue
    private int orderNumber;
    private String name;
    private String details;
    private Status status;
    private BigDecimal cost;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();
}

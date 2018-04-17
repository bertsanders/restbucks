package us.bertsanders.restbucks.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by bert on 2/7/17.
 */

@Data
@Entity
public class Payment {
    @Id
    @GeneratedValue
    private int transactionId;
    private BigDecimal paymentAmount;
}

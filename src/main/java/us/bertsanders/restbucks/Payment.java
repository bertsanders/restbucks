package us.bertsanders.restbucks;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by bert on 2/7/17.
 */

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private int transactionId;
    private BigDecimal paymentAmount;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}

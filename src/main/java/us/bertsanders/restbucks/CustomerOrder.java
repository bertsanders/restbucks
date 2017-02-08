package us.bertsanders.restbucks;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bert on 2/5/17.
 */

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

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

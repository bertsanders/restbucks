package us.bertsanders.restbucks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderNumber;
    private String name;
    private String details;
    private Status status;
}
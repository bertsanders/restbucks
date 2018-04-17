package us.bertsanders.restbucks.order;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.bertsanders.restbucks.model.CustomerOrder;
import us.bertsanders.restbucks.model.Payment;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
class OrderService {
    @NonNull
    private OrderRepository orderRepository;

    public CustomerOrder create(CustomerOrder customerOrder) {
        customerOrder.setStatus(CustomerOrder.Status.PAYMENT_EXPECTED);
        customerOrder.setCost(BigDecimal.valueOf(5)); //for simplicity, everything costs 5 units of currency
        return orderRepository.save(customerOrder);
    }

    public Iterable<CustomerOrder> lookupAll() {
        return orderRepository.findAll();
    }

    public CustomerOrder findByOrderNumber(int orderNumber) {
        return orderRepository.findOne(orderNumber);
    }

    public CustomerOrder updateOrder(int orderNumber, CustomerOrder updatedCustomerOrder) {
        CustomerOrder existingOrder = findByOrderNumber(orderNumber);
        if (!existingOrder.getStatus().equals(CustomerOrder.Status.PAYMENT_EXPECTED)) {
            throw new IllegalStateException("Cannot modify an order after payment is received");
        }
        updatedCustomerOrder.setOrderNumber(orderNumber);
        return orderRepository.save(updatedCustomerOrder);
    }

    public void deleteOrder(int orderNumber) {
        CustomerOrder existingOrder = findByOrderNumber(orderNumber);
        if (existingOrder.getStatus().equals(CustomerOrder.Status.TAKEN)) {
            throw new IllegalStateException("Cannot delete an order after it is taken");
        }
        orderRepository.delete(orderNumber);
    }

    public CustomerOrder acceptOrder(int orderNumber) {
        CustomerOrder customerOrder = findByOrderNumber(orderNumber);
        if (!customerOrder.getStatus().equals(CustomerOrder.Status.READY)) {
            throw new IllegalStateException("Cannot accept an order that is not ready");
        }
        customerOrder.setStatus(CustomerOrder.Status.TAKEN);
        return orderRepository.save(customerOrder);
    }

    public CustomerOrder prepareOrder(int orderNumber) {
        CustomerOrder customerOrder = findByOrderNumber(orderNumber);
        if (!customerOrder.getStatus().equals(CustomerOrder.Status.PAID)) {
            throw new IllegalStateException("Cannot prepare an order that has not been paid");
        }
        customerOrder.setStatus(CustomerOrder.Status.PREPARING);
        return orderRepository.save(customerOrder);
    }

    public CustomerOrder completeOrder(int orderNumber) {
        CustomerOrder customerOrder = findByOrderNumber(orderNumber);
        if (!customerOrder.getStatus().equals(CustomerOrder.Status.PREPARING)) {
            throw new IllegalStateException("Cannot complete an order that has is not preparing");
        }
        customerOrder.setStatus(CustomerOrder.Status.READY);
        return orderRepository.save(customerOrder);
    }

    @Transactional
    public CustomerOrder acceptPayment(int orderNumber, Payment payment) {
        CustomerOrder customerOrder = orderRepository.findOne(orderNumber);
        customerOrder.getPayments().add(payment);
        customerOrder = orderRepository.save(customerOrder);

        BigDecimal payments = customerOrder.getPayments().stream()
                .map(Payment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (payments.compareTo(customerOrder.getCost()) == 0)
        {
            customerOrder.setStatus(CustomerOrder.Status.PAID);
            customerOrder = orderRepository.save(customerOrder);
        }
        //TODO on overpayment, rollback transaction and generate bad request
        return customerOrder;
    }
}

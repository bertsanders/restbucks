package us.bertsanders.restbucks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by bert on 2/5/17.
 */

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired private OrderRepository orderRepository;

    @RequestMapping(path = "/hello")
    public String hello()
    {
        return "hello";
    }

    @RequestMapping(path = "/", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> create(@RequestBody CustomerOrder customerOrder)
    {
        customerOrder.setStatus(CustomerOrder.Status.PAYMENT_EXPECTED);
        customerOrder.setCost(BigDecimal.valueOf(5));
        return buildResource(orderRepository.save(customerOrder));
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<Resource<CustomerOrder>> lookupAll()
    {
        Iterable<CustomerOrder> orders = orderRepository.findAll();
        List<Resource<CustomerOrder>> resourceList = StreamSupport.stream(orders.spliterator(), false).map(o -> buildResource(o))
                .collect(Collectors.toList());

        Resources<Resource<CustomerOrder>> resources = new Resources<>(resourceList);
        resources.add(new Link("/order", "self"));
        return resources;
    }

    @RequestMapping(path = "/{orderNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> lookupOrder(@PathVariable int orderNumber)
    {
        CustomerOrder customerOrder = orderRepository.findOne(orderNumber);
        Resource<CustomerOrder> resource = buildResource(customerOrder);
        return resource;
    }

    private Resource<CustomerOrder> buildResource(CustomerOrder customerOrder) {
        Resource<CustomerOrder> resource = new Resource<>(customerOrder);
        resource.add(new Link("/order/"+  customerOrder.getOrderNumber(), "self"));
        resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/payment", "payment"));
        resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/accept", "accept"));
        resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/prepare", "prepare"));
        resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/complete", "complete"));
        return resource;
    }

    @RequestMapping(path = "/{orderNumber}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> updateOrder(@PathVariable int orderNumber, @RequestBody CustomerOrder customerOrder)
    {
        CustomerOrder existingOrder = lookupOrder(orderNumber).getContent();
        if (!existingOrder.getStatus().equals(CustomerOrder.Status.PAYMENT_EXPECTED))
        {
            throw new IllegalStateException("Cannot modify an order after payment is received");
        }
        customerOrder.setOrderNumber(orderNumber);
        return buildResource(orderRepository.save(customerOrder));
    }

    @RequestMapping(path = "/{orderNumber}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteOrder(@PathVariable int orderNumber)
    {
        CustomerOrder existingOrder = lookupOrder(orderNumber).getContent();
        if (existingOrder.getStatus().equals(CustomerOrder.Status.TAKEN))
        {
            throw new IllegalStateException("Cannot delete an order after it is taken");
        }
        orderRepository.delete(orderNumber);
    }

    @RequestMapping(path = "/{orderNumber}/accept", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> acceptOrder(@PathVariable int orderNumber)
    {
        CustomerOrder customerOrder = lookupOrder(orderNumber).getContent();
        if (!customerOrder.getStatus().equals(CustomerOrder.Status.READY))
        {
            throw new IllegalStateException("Cannot accept an order that is not ready");
        }
        customerOrder.setStatus(CustomerOrder.Status.TAKEN);
        return buildResource(orderRepository.save(customerOrder));
    }

    @RequestMapping(path = "/{orderNumber}/prepare", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> prepareOrder(@PathVariable int orderNumber)
    {
        CustomerOrder customerOrder = lookupOrder(orderNumber).getContent();
        if (!customerOrder.getStatus().equals(CustomerOrder.Status.PAID))
        {
            throw new IllegalStateException("Cannot prepare an order that has not been paid");
        }
        customerOrder.setStatus(CustomerOrder.Status.PREPARING);
        return buildResource(orderRepository.save(customerOrder));
    }

    @RequestMapping(path = "/{orderNumber}/complete", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> completeOrder(@PathVariable int orderNumber)
    {
        CustomerOrder customerOrder = lookupOrder(orderNumber).getContent();
        if (!customerOrder.getStatus().equals(CustomerOrder.Status.PREPARING))
        {
            throw new IllegalStateException("Cannot complete an order that has is not preparing");
        }
        customerOrder.setStatus(CustomerOrder.Status.READY);
        return buildResource(orderRepository.save(customerOrder));
    }

    @RequestMapping(path = "/{orderNumber}/payment", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> acceptPayment(@PathVariable("orderNumber") int orderNumber, @RequestBody Payment payment)
    {
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
        return buildResource(customerOrder);
    }

}

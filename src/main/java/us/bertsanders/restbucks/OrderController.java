package us.bertsanders.restbucks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    public CustomerOrder create(@RequestBody CustomerOrder customerOrder)
    {
        customerOrder.setStatus(CustomerOrder.Status.PAYMENT_EXPECTED);
        customerOrder.setCost(BigDecimal.valueOf(5));
        return orderRepository.save(customerOrder);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<CustomerOrder> lookupAll()
    {
        return orderRepository.findAll();
    }

    @RequestMapping(path = "/{orderNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerOrder lookupOrder(@PathVariable int orderNumber)
    {
        return orderRepository.findOne(orderNumber);
    }

    @RequestMapping(path = "/{orderNumber}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerOrder updateOrder(@PathVariable int orderNumber, @RequestBody CustomerOrder customerOrder)
    {
        CustomerOrder existingOrder = lookupOrder(orderNumber);
        if (!existingOrder.getStatus().equals(CustomerOrder.Status.PAYMENT_EXPECTED))
        {
            throw new IllegalStateException("Cannot modify an order after payment is received");
        }
        customerOrder.setOrderNumber(orderNumber);
        return orderRepository.save(customerOrder);
    }

    @RequestMapping(path = "/{orderNumber}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteOrder(@PathVariable int orderNumber)
    {
        CustomerOrder existingOrder = lookupOrder(orderNumber);
        if (existingOrder.getStatus().equals(CustomerOrder.Status.TAKEN))
        {
            throw new IllegalStateException("Cannot delete an order after it is taken");
        }
        orderRepository.delete(orderNumber);
    }

    @RequestMapping(path = "/{orderNumber}/accept", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerOrder acceptOrder(@PathVariable int orderNumber)
    {
        CustomerOrder customerOrder = lookupOrder(orderNumber);
        if (!customerOrder.getStatus().equals(CustomerOrder.Status.READY))
        {
            throw new IllegalStateException("Cannot accept an order that is not ready");
        }
        customerOrder.setStatus(CustomerOrder.Status.TAKEN);
        return orderRepository.save(customerOrder);
    }

}

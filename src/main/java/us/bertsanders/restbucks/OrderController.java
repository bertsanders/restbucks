package us.bertsanders.restbucks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        customerOrder.setStatus(CustomerOrder.Status.PREPARING);
        return orderRepository.save(customerOrder);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<CustomerOrder> lookupAll()
    {
        return orderRepository.findAll();
    }
}

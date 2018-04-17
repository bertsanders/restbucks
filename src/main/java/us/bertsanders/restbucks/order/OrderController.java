package us.bertsanders.restbucks.order;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import us.bertsanders.restbucks.model.CustomerOrder;
import us.bertsanders.restbucks.model.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by bert on 2/5/17.
 */

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    @NonNull private OrderService orderService;

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> create(@RequestBody CustomerOrder customerOrder)
    {
        return buildResource(orderService.create(customerOrder));
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<Resource<CustomerOrder>> lookupAll()
    {
        Iterable<CustomerOrder> orders = orderService.lookupAll();
        List<Resource<CustomerOrder>> resourceList = StreamSupport.stream(orders.spliterator(), false).map(o -> buildResource(o))
                .collect(Collectors.toList());

        Resources<Resource<CustomerOrder>> resources = new Resources<>(resourceList);
        resources.add(new Link("/order/", "self"));
        return resources;
    }

    @GetMapping(path = "/{orderNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> lookupOrder(@PathVariable int orderNumber)
    {
        CustomerOrder customerOrder = orderService.findByOrderNumber(orderNumber);
        Resource<CustomerOrder> resource = buildResource(customerOrder);
        return resource;
    }

    @RequestMapping(path = "/{orderNumber}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> updateOrder(@PathVariable int orderNumber, @RequestBody CustomerOrder customerOrder)
    {
        return buildResource(orderService.updateOrder(orderNumber, customerOrder));
    }

    @DeleteMapping(path = "/{orderNumber}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteOrder(@PathVariable int orderNumber)
    {
        orderService.deleteOrder(orderNumber);
    }

    @PutMapping(path = "/{orderNumber}/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> acceptOrder(@PathVariable int orderNumber)
    {
        return buildResource(orderService.acceptOrder(orderNumber));
    }

    @PutMapping(path = "/{orderNumber}/prepare", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> prepareOrder(@PathVariable int orderNumber)
    {
        return buildResource(orderService.prepareOrder(orderNumber));
    }

    @PutMapping(path = "/{orderNumber}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> completeOrder(@PathVariable int orderNumber)
    {
        return buildResource(orderService.completeOrder(orderNumber));
    }

    @RequestMapping(path = "/{orderNumber}/payment", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<CustomerOrder> acceptPayment(@PathVariable("orderNumber") int orderNumber, @RequestBody Payment payment)
    {
        return buildResource(orderService.acceptPayment(orderNumber, payment));
    }

    private Resource<CustomerOrder> buildResource(CustomerOrder customerOrder) {
        Resource<CustomerOrder> resource = new Resource<>(customerOrder);
        resource.add(new Link("/order/"+  customerOrder.getOrderNumber(), "self"));
        if (customerOrder.getStatus().equals(CustomerOrder.Status.PAYMENT_EXPECTED))
            resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/payment", "payment"));

        if (customerOrder.getStatus().equals(CustomerOrder.Status.PAID))
            resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/prepare", "prepare"));

        if (customerOrder.getStatus().equals(CustomerOrder.Status.PREPARING))
            resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/complete", "complete"));

        if (customerOrder.getStatus().equals(CustomerOrder.Status.READY))
            resource.add(new Link("/order/"+  customerOrder.getOrderNumber() + "/accept", "accept"));

        return resource;
    }


}

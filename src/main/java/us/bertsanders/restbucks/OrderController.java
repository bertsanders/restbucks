package us.bertsanders.restbucks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

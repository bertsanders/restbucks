package us.bertsanders.restbucks.index;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by bert on 2/7/17.
 */

@RestController
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<String> index()
    {
        Resources<String> resources = new Resources<>(new ArrayList<>(),
                new Link("/order/", "order"));
        return resources;
    }
}

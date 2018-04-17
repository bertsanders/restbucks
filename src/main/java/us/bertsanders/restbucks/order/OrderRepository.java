package us.bertsanders.restbucks.order;

import org.springframework.data.repository.CrudRepository;
import us.bertsanders.restbucks.model.CustomerOrder;

/**
 * Created by bert on 2/5/17.
 */
public interface OrderRepository extends CrudRepository<CustomerOrder, Integer> {
}

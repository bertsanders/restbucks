package us.bertsanders.restbucks;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by bert on 2/5/17.
 */
public interface OrderRepository extends CrudRepository<Order, Integer> {
}

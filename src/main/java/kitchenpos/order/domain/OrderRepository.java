package kitchenpos.order.domain;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, Collection<OrderStatus> orderStatus);


}

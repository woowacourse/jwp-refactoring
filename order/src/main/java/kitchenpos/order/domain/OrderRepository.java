package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();

    List<Order> findAllByOrderTableId(AggregateReference<OrderTable, Long> orderTableId);

}

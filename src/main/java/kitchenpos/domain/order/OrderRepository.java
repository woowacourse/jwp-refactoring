package kitchenpos.domain.order;

import java.util.List;

public interface OrderRepository {

    Order add(Order order);

    Order get(Long id);

    List<Order> getByOrderTableId(Long orderTableId);

    List<Order> getOrderTableIdsIn(List<Long> ids);

    List<Order> getAll();
}

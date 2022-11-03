package kitchenpos.domain.order;

import java.util.List;

public interface OrderRepository {

    Order add(Order order);

    Order get(Long id);

    List<Order> getAll();
}

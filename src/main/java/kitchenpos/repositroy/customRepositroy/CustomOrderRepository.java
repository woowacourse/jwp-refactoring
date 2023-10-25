package kitchenpos.repositroy.customRepositroy;

import java.util.List;
import kitchenpos.domain.order.Order;

public interface CustomOrderRepository {

    List<Order> findAllByFetch();
}

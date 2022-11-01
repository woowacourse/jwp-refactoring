package kitchenpos.repository;

import kitchenpos.domain.order.Order;
import org.springframework.data.repository.Repository;

public interface JpaOrderRepository extends Repository<Order, Long>, OrderRepository {
}

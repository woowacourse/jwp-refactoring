package kitchenpos.order.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

}

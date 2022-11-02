package kitchenpos.order.repository;

import kitchenpos.order.domain.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuDao extends JpaRepository<OrderMenu, Long> {
}

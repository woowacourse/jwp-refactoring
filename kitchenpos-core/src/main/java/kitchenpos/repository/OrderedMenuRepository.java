package kitchenpos.repository;

import kitchenpos.domain.order.OrderedMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedMenuRepository extends JpaRepository<OrderedMenu, Long> {
}

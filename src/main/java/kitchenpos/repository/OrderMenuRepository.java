package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.order.OrderMenu;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
}

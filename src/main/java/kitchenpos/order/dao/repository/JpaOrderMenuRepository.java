package kitchenpos.order.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.OrderMenu;

public interface JpaOrderMenuRepository extends JpaRepository<OrderMenu, Long> {
}

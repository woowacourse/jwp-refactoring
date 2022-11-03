package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.order.Order;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {
}

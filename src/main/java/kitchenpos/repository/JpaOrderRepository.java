package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.order.Orders;

public interface JpaOrderRepository extends JpaRepository<Orders, Long> {
}

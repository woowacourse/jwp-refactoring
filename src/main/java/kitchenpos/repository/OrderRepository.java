package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

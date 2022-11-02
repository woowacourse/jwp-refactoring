package kitchenpos.order.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.MenuOrder;

public interface JpaMenuOrderRepository extends JpaRepository<MenuOrder, Long> {
}

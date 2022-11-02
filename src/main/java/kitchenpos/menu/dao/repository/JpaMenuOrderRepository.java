package kitchenpos.menu.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.MenuOrder;

public interface JpaMenuOrderRepository extends JpaRepository<MenuOrder, Long> {
}

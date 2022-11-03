package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menu.Menu;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {
}

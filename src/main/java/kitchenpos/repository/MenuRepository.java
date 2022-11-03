package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menu.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}

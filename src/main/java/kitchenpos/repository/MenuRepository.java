package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}

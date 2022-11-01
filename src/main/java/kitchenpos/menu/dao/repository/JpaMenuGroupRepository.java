package kitchenpos.menu.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.MenuGroup;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}

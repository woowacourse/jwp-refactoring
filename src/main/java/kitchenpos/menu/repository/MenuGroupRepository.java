package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}

package kitchenpos.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menu.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}

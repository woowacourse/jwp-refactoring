package kitchenpos.core.domain.menu.repository;

import kitchenpos.core.domain.menu.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}

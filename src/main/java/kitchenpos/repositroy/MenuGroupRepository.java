package kitchenpos.repositroy;

import kitchenpos.domain.menu_group.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}

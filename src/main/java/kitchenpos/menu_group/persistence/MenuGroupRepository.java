package kitchenpos.menu_group.persistence;

import kitchenpos.menu_group.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

}

package kitchenpos.menu_group.repository;

import kitchenpos.menu_group.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

}

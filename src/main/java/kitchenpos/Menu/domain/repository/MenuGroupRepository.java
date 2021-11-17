package kitchenpos.Menu.domain.repository;

import kitchenpos.Menu.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}

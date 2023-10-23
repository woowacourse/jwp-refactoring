package kitchenpos.menu.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    default MenuGroup getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] MenuGroup이 존재하지 않습니다. id : " + id));
    }
}

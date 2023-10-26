package kitchenpos.repository;

import kitchenpos.domain.menugroup.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    default MenuGroup getMenuGroupById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("[ERROR] 메뉴그룹이 존재하지 않습니다."));
    }
}

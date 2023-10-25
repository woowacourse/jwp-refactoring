package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(final Long menuGroupId) {
        return findById(menuGroupId)
                .orElseThrow(() -> new RuntimeException("해당 메뉴 그룹 ID가 존재하지 않습니다."));
    }
}

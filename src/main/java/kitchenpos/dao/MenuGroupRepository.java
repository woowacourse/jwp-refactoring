package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(Long menuGroupId) {
        return findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴 그룹 ID가 존재하지 않습니다."));
    }
}

package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(final Long menuGroupId) {
        return findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
    }
}

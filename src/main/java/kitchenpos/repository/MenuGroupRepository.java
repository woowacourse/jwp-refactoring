package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup findMenuGroupById(final Long menuGroupId) {
        return findById(menuGroupId).orElseThrow(() -> new IllegalArgumentException("메뉴 그룹을 식별자 값으로 조회할 수 없습니다."));
    }
}

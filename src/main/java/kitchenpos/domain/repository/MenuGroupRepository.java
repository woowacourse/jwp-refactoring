package kitchenpos.domain.repository;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(final Long menuGroupId) {
        return findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("id가 " + menuGroupId + "인 MenuGroup를 찾을 수 없습니다!"));
    }

}

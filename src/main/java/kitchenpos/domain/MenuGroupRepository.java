package kitchenpos.domain;

import static kitchenpos.exception.MenuGroupExceptionType.MENU_GROUP_NOT_FOUND;

import kitchenpos.exception.MenuGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    @Override
    default MenuGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new MenuGroupException(MENU_GROUP_NOT_FOUND));
    }
}

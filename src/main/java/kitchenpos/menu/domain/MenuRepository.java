package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.exception.MenuExceptionType.MENU_IS_NOT_FOUND;

import java.util.List;
import kitchenpos.menu.domain.exception.MenuException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    int countByIdIn(List<Long> menuIds);

    default Menu getById(final Long id) {
        return findById(id).orElseThrow(() -> new MenuException(MENU_IS_NOT_FOUND));
    }
}

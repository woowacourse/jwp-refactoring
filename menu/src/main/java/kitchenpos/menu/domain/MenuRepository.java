package kitchenpos.menu.domain;

import static kitchenpos.menu.exception.MenuExceptionType.MENU_NOT_FOUND;

import java.util.List;
import kitchenpos.menu.exception.MenuException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Override
    default Menu getById(Long id) {
        return findById(id).orElseThrow(() -> new MenuException(MENU_NOT_FOUND));
    }

    long countByIdIn(List<Long> menuIds);
}

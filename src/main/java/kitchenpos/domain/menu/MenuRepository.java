package kitchenpos.domain.menu;

import static kitchenpos.exception.menu.MenuExceptionType.MENU_NOT_FOUND;

import java.util.List;
import kitchenpos.exception.menu.MenuException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Override
    default Menu getById(Long id) {
        return findById(id).orElseThrow(() -> new MenuException(MENU_NOT_FOUND));
    }

    long countByIdIn(List<Long> menuIds);
}

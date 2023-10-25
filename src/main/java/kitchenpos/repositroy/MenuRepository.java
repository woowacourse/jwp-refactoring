package kitchenpos.repositroy;

import kitchenpos.domain.menu.Menu;
import kitchenpos.exception.MenuException;
import kitchenpos.repositroy.customRepositroy.CustomMenuRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, CustomMenuRepository {

    default Menu getById(final Long id) {
        return findById(id).orElseThrow(() -> new MenuException.NotFoundException(id));
    }
}

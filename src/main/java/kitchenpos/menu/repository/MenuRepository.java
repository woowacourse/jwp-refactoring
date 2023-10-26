package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.exception.MenuException.NotExistsMenuException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByDeletedFalse();

    Long countByIdIn(List<Long> ids);

    default Menu getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new NotExistsMenuException(id));
    }
}

package kitchenpos.domain.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getById(final Long menuId) {
        return findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
    }

}

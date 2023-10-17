package kitchenpos.domain.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(final Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

}

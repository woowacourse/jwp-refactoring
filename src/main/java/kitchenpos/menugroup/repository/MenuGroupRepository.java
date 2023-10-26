package kitchenpos.menugroup.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.exception.MenuGroupException.NotExistsMenuGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new NotExistsMenuGroupException(id));
    }
}

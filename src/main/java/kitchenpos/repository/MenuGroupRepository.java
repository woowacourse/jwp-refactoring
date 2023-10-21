package kitchenpos.repository;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.exception.MenuGroupException.NotExistsMenuGroupException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new NotExistsMenuGroupException(id));
    }
}

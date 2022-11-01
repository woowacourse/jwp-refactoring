package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.exception.badrequest.MenuGroupNotFoundException;
import org.springframework.data.repository.Repository;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {
    MenuGroup save(MenuGroup entity);

    List<MenuGroup> findAll();

    default MenuGroup getById(Long menuGroupId) {
        return findById(menuGroupId).orElseThrow(MenuGroupNotFoundException::new);
    }

    Optional<MenuGroup> findById(Long menuGroupId);
}

package kitchenpos.menugroup.domain.repository;

import kitchenpos.menugroup.domain.MenuGroup;

import java.util.List;
import java.util.Optional;

public interface MenuGroupRepository {

    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}

package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.*;

import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(MenuGroup menuGroup);

    List<MenuGroup> findAll();

    boolean existsById(Long id);

    Optional<MenuGroup> findById(Long menuGroupId);
}

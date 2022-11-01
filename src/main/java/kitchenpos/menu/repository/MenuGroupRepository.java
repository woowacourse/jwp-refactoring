package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(MenuGroup menuGroup);

    List<MenuGroup> findAll();

    Optional<MenuGroup> findById(Long menuGroupId);
}

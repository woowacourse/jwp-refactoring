package kitchenpos.menu.domain.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(final MenuGroup menuGroup);

    List<MenuGroup> findAll();

    boolean existsById(final Long id);
}

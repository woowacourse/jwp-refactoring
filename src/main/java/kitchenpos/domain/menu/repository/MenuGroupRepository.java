package kitchenpos.domain.menu.repository;

import kitchenpos.domain.menu.MenuGroup;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(final MenuGroup menuGroup);

    List<MenuGroup> findAll();

    boolean existsById(final Long id);
}

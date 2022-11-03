package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.MenuGroup;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(final MenuGroup menuGroup);

    List<MenuGroup> findAll();

    boolean existsById(final Long id);
}

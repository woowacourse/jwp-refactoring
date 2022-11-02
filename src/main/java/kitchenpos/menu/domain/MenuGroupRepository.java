package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();
}

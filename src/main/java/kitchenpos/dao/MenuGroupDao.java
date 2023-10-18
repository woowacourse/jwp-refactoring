package kitchenpos.dao;

import kitchenpos.domain.menu.MenuGroup;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MenuGroupDao extends Repository<MenuGroup, Long> {
    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}

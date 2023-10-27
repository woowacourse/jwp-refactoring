package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.repository.Repository;

public interface MenuGroupDao extends Repository<MenuGroup, Long> {
    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();
}

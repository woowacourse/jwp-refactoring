package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.repository.Repository;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {
    MenuGroup save(MenuGroup entity);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}

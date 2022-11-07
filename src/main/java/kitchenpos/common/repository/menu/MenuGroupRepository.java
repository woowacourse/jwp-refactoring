package kitchenpos.common.repository.menu;

import java.util.List;
import kitchenpos.common.domain.menu.MenuGroup;
import org.springframework.data.repository.Repository;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(MenuGroup entity);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}

package kitchenpos.core.menu.application;

import java.util.List;
import kitchenpos.core.menu.domain.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuDao extends CrudRepository<Menu, Long> {
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}

package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Menu;

public interface MenuDao {
    Menu save(Menu entity);

    Menu findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}

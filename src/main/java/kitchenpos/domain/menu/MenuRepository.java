package kitchenpos.domain.menu;

import java.util.List;

public interface MenuRepository {

    Menu add(Menu menu);

    List<Menu> getAll();

    Menu get(Long id);
}

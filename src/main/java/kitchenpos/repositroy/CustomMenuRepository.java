package kitchenpos.repositroy;

import java.util.List;
import kitchenpos.domain.menu.Menu;

public interface CustomMenuRepository {

    List<Menu> findAllByFetch();
}

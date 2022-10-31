package kitchenpos.repository.menu;

import java.util.List;
import kitchenpos.domain.menu.Menu;

public interface MenuRepositoryCustom  {

    List<Menu> findAllWithMenuProduct();
}

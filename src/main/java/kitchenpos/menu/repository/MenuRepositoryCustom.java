package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;

public interface MenuRepositoryCustom  {

    List<Menu> findAllWithMenuProduct();
}

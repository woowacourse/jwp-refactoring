package kitchenpos.dao.jpa;

import java.util.List;
import kitchenpos.domain.Menu;

public interface MenuRepositoryCustom  {

    List<Menu> findAllWithMenuProduct();
}

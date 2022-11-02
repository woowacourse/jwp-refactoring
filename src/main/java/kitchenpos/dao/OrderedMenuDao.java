package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderedMenu;

public interface OrderedMenuDao {

   OrderedMenu save(OrderedMenu orderedMenu);

   long countByIdIn(List<Long> ids);
}

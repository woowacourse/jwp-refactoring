package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderMenu;

public interface OrderMenuDao {

    OrderMenu save(OrderMenu entity);

    Optional<OrderMenu> findById(Long id);

    List<OrderMenu> findAll();

    long countByIdIn(List<Long> ids);

    List<OrderMenu> findByIdIn(List<Long> menuIds);
}

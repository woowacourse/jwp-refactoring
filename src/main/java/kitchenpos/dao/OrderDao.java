package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;

public interface OrderDao {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    Optional<Order> findByOrderTableId(Long id);

    List<Order> findAllByOrderTableIdIn(List<Long> tableGroupIds);
}

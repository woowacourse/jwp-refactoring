package kitchenpos.repository.order;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();
}

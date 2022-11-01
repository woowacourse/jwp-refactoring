package kitchenpos.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.exception.badrequest.OrderIdInvalidException;
import kitchenpos.exception.notfound.OrderNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order entity);

    default Order getById(Long id) {
        if (Objects.isNull(id)) {
            throw new OrderIdInvalidException(id);
        }
        return findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = {"orderLineItems"})
    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}

package kitchenpos.repositroy;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.exception.OrderException;
import kitchenpos.repositroy.customRepositroy.CustomOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {

    default Order getById(final Long id) {
        return findById(id).orElseThrow(() -> new OrderException.NotFoundException(id));
    }

    List<Order> findByOrderTableId(final Long orderTableId);
}

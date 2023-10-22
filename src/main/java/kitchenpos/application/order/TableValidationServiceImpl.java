package kitchenpos.application.order;

import java.util.List;
import kitchenpos.application.table.TableValidationService;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.domain.order.Order;
import org.springframework.stereotype.Component;

@Component
public class TableValidationServiceImpl implements TableValidationService {

    private final OrderRepository orderRepository;

    public TableValidationServiceImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeEmptyAvailable(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        validateOrdersStatus(orders);
    }

    private void validateOrdersStatus(final List<Order> orders) {
        if (orders.stream().allMatch(Order::isCompleted)) {
            return;
        }
        throw new IllegalArgumentException("Cannot change empty status of table with order status not completion");
    }

    @Override
    public void validateUngroupAvailable(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (!orders.stream().allMatch(Order::isCompleted)) {
            throw new IllegalArgumentException("Cannot ungroup non-completed table.");
        }
    }
}

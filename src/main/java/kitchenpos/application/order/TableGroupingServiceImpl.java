package kitchenpos.application.order;

import java.util.List;
import kitchenpos.application.table.TableGroupingService;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.domain.order.Order;
import org.springframework.stereotype.Component;

@Component
public class TableGroupingServiceImpl implements TableGroupingService {

    private final OrderRepository orderRepository;

    public TableGroupingServiceImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void isAbleToGroup(final Long orderTableId) {
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
    public void isAbleToUngroup(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (!orders.stream().allMatch(Order::isCompleted)) {
            throw new IllegalArgumentException("Cannot ungroup non-completed table.");
        }
    }
}

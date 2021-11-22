package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableEmptyChangeService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class DefaultTableEmptyChangeService implements TableEmptyChangeService {

    private final OrderRepository orderRepository;

    public DefaultTableEmptyChangeService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean canChangeEmpty(final OrderTable orderTable) {
        return !existWithOrderTableAndContainsOrderStatusIn(orderTable,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    private boolean existWithOrderTableAndContainsOrderStatusIn(final OrderTable orderTable,
                                                               final List<OrderStatus> orderStatuses) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        return orders.stream()
            .anyMatch(order -> order.isOrderStatusIn(orderStatuses));
    }
}

package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableEmptyChangedEvent;
import kitchenpos.table.application.UngroupedEvent;
import kitchenpos.table.domain.OrderTable;

@Service
@Transactional
public class OrderEventHandler {

    private final OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener(TableEmptyChangedEvent.class)
    public void validateTableExistInNotCompletedStatus(TableEmptyChangedEvent event) {

        OrderTable orderTable = event.getOrderTable();

        if (orderRepository.existsByOrderTableAndOrderStatusIn(
            orderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @EventListener(UngroupedEvent.class)
    public void validateTablesExistInNotCompletedStatus(UngroupedEvent event) {

        List<OrderTable> orderTables = event.getOrderTables();

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}

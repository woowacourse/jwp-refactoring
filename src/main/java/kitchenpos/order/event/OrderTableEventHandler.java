package kitchenpos.order.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;

@Component
public class OrderTableEventHandler {

    private static final List<String> ORDER_STATUS_FOR_CANT_UNGROUP = new ArrayList<String>() {{
        add(OrderStatus.COOKING.name());
        add(OrderStatus.MEAL.name());
    }};

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    private void ungroup(UngroupEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(event.getTableGroupId());
        final List<Long> orderTableIds = orderTables.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, ORDER_STATUS_FOR_CANT_UNGROUP)) {
            throw new IllegalArgumentException("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

}

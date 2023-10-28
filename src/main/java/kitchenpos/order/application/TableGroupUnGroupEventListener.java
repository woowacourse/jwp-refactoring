package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.TableGroupUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TableGroupUnGroupEventListener {

    private final OrderRepository orderRepository;

    public TableGroupUnGroupEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateOrderStatus(TableGroupUngroupEvent tableGroupUngroupEvent) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroupUngroupEvent.getTableGroupIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 주문 그룹을 해제할 수 없습니다.");
        }
    }
}

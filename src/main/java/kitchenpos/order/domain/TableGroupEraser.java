package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupDeletable;
import org.springframework.stereotype.Component;

@Component
public class TableGroupEraser implements TableGroupDeletable {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupEraser(OrderRepository orderRepository,
                            OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void ungroup(Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        final List<Long> orderTableIds = orderTables.getOrderTableIds();
        orderRepository.existsByOrderTableIdIn(orderTableIds);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        orderTables.update(null, false);
    }
}

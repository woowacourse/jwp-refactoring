package kitchenpos.tablegroup.service;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validatePossibleUngrouping(final TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTables(), List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사 중인 테이블이 있습니다.");
        }
    }
}

package kitchenpos.tablegroup.service;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validatePossibleChangeToEmpty(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사 중인 테이블 입니다.");
        }
    }

    public void validatePossibleUngrouping(final TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTables(), List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사 중인 테이블이 있습니다.");
        }
    }
}

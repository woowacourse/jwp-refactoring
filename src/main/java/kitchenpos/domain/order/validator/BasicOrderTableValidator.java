package kitchenpos.domain.order.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.validator.OrderTableValidator;
import kitchenpos.domain.tablegroup.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class BasicOrderTableValidator implements OrderTableValidator {
    private final OrderRepository orderRepository;

    public BasicOrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateAbleToChangeEmpty(final boolean empty, final OrderTable orderTable) {
        validateOrderStatus(orderTable);
        validateIsGrouped(empty, orderTable);
    }

    private void validateOrderStatus(final OrderTable orderTable) {
        if (!isCompletedOrder(List.of(orderTable.getId()))) {
            throw new IllegalArgumentException("테이블의 주문이 완료되지 않아 빈 테이블로 변경할 수 없습니다.");
        }
    }

    private void validateIsGrouped(final boolean empty, final OrderTable orderTable) {
        if (empty && orderTable.isGrouped()) {
            throw new IllegalArgumentException("그룹으로 지정된 테이블은 빈 상태로 변경할 수 없습니다.");
        }
    }

    @Override
    public void validateAbleToUngroup(final TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        validateOrderStatus(orderTableIds);
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        if (!isCompletedOrder(orderTableIds)) {
            throw new IllegalArgumentException("테이블의 주문이 완료되지 않아 단체 지정을 취소할 수 없습니다.");
        }
    }

    private boolean isCompletedOrder(final List<Long> orderTableIds) {
        return !orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}

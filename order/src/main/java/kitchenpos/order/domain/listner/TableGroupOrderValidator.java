package kitchenpos.order.domain.listner;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.order.domain.exception.OrderExceptionType;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table_group.domain.UngroupTableValidator;
import org.springframework.stereotype.Component;

@Component
public class TableGroupOrderValidator implements UngroupTableValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupOrderValidator(
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateUngroup(final Long tableGroupId) {
        final List<Long> orderTableIdsInTableGroup
            = findOrderTableIdsInTableGroup(tableGroupId);

        if (isOrderStatusIsNotCompletion(orderTableIdsInTableGroup)) {
            throw new OrderException(OrderExceptionType.ORDER_IS_NOT_COMPLETION);
        }
    }

    private boolean isOrderStatusIsNotCompletion(final List<Long> orderTableIdsInTableGroup) {
        return orderRepository.findByOrderTableIdIn(orderTableIdsInTableGroup)
            .stream()
            .anyMatch(Order::isNotAlreadyCompletion);
    }

    private List<Long> findOrderTableIdsInTableGroup(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId)
            .stream()
            .map(OrderTable::getId)
            .collect(toUnmodifiableList());
    }
}

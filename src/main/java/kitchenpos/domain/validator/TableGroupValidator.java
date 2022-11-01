package kitchenpos.domain.validator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {
    private static final List<OrderStatus> ACTIVE_ORDER_STATUS = List.of(OrderStatus.COOKING, OrderStatus.MEAL);

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void validateUnGroup(final TableGroup tableGroup) {
        final var tableGroupId = tableGroup.getId();
        final var orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateTableGroupId(orderTables, tableGroupId);

        final var orderTableIds = extractTableIds(orderTables);
        validateAllOrdersInGroupedTableAreComplete(orderTableIds);
    }


    private void validateTableGroupId(final List<OrderTable> orderTables, final Long tableGroupId) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("유효하지 않은 그룹 아이디 : " + tableGroupId);
        }
    }

    private List<Long> extractTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateAllOrdersInGroupedTableAreComplete(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, ACTIVE_ORDER_STATUS)) {
            throw new IllegalArgumentException();
        }
    }
}

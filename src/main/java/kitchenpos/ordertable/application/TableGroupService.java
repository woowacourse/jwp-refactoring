package kitchenpos.ordertable.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ordertable.ui.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<Long> orderTableIds = tableGroupCreateRequest.mapToOrderTableIds();

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        tableGroup.validateOrderTableSize(orderTableIds);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.validateToCreate();
        }

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateOrderStatus(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableRepository.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 존재합니다.");
        }
    }
}

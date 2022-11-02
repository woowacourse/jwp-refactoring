package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exception.OrderNotCompletionException;
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
        final TableGroup tableGroup = tableGroupCreateRequest.toTableGroup();
        final List<OrderTable> savedOrderTables = findOrderTables(tableGroup);
        tableGroup.validateExistOrderTable(savedOrderTables.size());

        final TableGroup newTableGroup = new TableGroup(null, LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupRepository.save(newTableGroup);

        final List<OrderTable> groupedOrderTables = groupOrderTable(savedOrderTables, savedTableGroup);

        savedTableGroup.updateOrderTables(groupedOrderTables);
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> findOrderTables(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private List<OrderTable> groupOrderTable(final List<OrderTable> savedOrderTables, final TableGroup tableGroup) {
        final List<OrderTable> newOrderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.groupTableBy(tableGroup);
            newOrderTables.add(orderTableRepository.save(savedOrderTable));
        }
        return newOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateOrderTableNotInCompletion(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateOrderTableNotInCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderNotCompletionException();
        }
    }
}

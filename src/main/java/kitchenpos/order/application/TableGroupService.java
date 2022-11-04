package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderTableIdDto;
import kitchenpos.order.application.dto.TableGroupCreationDto;
import kitchenpos.order.application.dto.TableGroupDto;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(final TableGroupCreationDto tableGroupCreationDto) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupCreationDto);
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateTheNumberOfOrderTableIds(orderTableIds, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables, LocalDateTime.now()));
        final Long tableGroupId = savedTableGroup.getId();

        final List<OrderTable> orderTables = savedOrderTables.stream()
                .map(orderTable -> orderTableRepository.save(
                        new OrderTable(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), false)))
                .collect(Collectors.toList());

        return TableGroupDto.from(savedTableGroup.addTableGroups(orderTables));
    }

    private List<Long> getOrderTableIds(final TableGroupCreationDto tableGroupCreationDto) {
        return tableGroupCreationDto.getOrderTableIds()
                .stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());
    }

    private void validateTheNumberOfOrderTableIds(final List<Long> orderTableIds,
                                                  final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }


    @Transactional
    public void ungroupTable(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrdersStatus(orderRepository.findAllByOrderTableId(orderTableIds));

        for (final OrderTable orderTable : orderTables) {
            orderTableRepository.save(new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false));
        }
    }

    private void validateOrdersStatus(final List<Order> orders) {
        if (isUnCompletionStatus(orders)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isUnCompletionStatus(final List<Order> orders) {
        return orders.stream()
                .anyMatch(order -> !order.isInCompletionStatus());
    }
}

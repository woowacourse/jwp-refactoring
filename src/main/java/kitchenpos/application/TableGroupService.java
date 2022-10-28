package kitchenpos.application;

import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = new OrderTables(tableGroupRequest.getOrderTables()
                .stream()
                .map(it -> orderTableRepository.findById(it).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList()));

        if (!orderTables.isPossibleTableGroup()) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = mapToOrderTableIds(orderTables);
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllById(orderTableIds));
        if (!savedOrderTables.isSameSize(orderTables.getOrderTablesSize())) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables.getOrderTables()) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.isNonNullTableGroup()) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup tableGroup = new TableGroup(tableGroupRequest.getCreatedDate(), orderTables.getOrderTables());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        final List<OrderTable> target = savedOrderTables.getOrderTables()
                .stream()
                .map(orderTable -> new OrderTable(orderTable.getId(), orderTable.getTableGroup(),
                        orderTable.getNumberOfGuests(), false))
                .collect(Collectors.toList());

        orderTableRepository.saveAll(target);

        return savedTableGroup;
    }

    private List<Long> mapToOrderTableIds(OrderTables orderTables) {
        return orderTables.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTableRepository.save(new OrderTable(orderTable.getId(), null,
                    orderTable.getNumberOfGuests(), orderTable.isEmpty()));
        }
    }
}

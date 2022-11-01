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
        final OrderTables orderTables = createOrderTables(tableGroupRequest);
        orderTables.validatePossibleBindTableGroup();

        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllById(mapToIds(orderTables)));
        orderTables.validateMakeTableGroup(orderTables.getOrderTablesSize());

        final TableGroup tableGroup = new TableGroup(tableGroupRequest.getCreatedDate(), orderTables.getOrderTables());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTableRepository.saveAll(getOrderTables(savedOrderTables).getOrderTables());

        return savedTableGroup;
    }

    private OrderTables getOrderTables(OrderTables savedOrderTables) {
        return new OrderTables(savedOrderTables.getOrderTables()
                .stream()
                .map(orderTable -> new OrderTable(orderTable.getId(), orderTable.getTableGroup(),
                        orderTable.getNumberOfGuests(), false))
                .collect(Collectors.toList()));
    }

    private OrderTables createOrderTables(TableGroupRequest tableGroupRequest) {
        return new OrderTables(tableGroupRequest.getOrderTables()
                .stream()
                .map(it -> orderTableRepository.findById(it)
                        .orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList()));
    }

    private List<Long> mapToIds(OrderTables orderTables) {
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

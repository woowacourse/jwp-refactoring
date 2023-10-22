package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.request.CreateTableGroupRequest.TableInfo;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
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

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public CreateTableGroupResponse create(final CreateTableGroupRequest createTableGroupRequest) {
        List<OrderTable> orderTables = findOrderTables(createTableGroupRequest.getOrderTables());
        validateOrderTables(createTableGroupRequest, orderTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.addOrderTables(orderTables);

        tableGroupRepository.save(tableGroup);
        return CreateTableGroupResponse.from(tableGroup);
    }

    private void validateOrderTables(CreateTableGroupRequest createTableGroupRequest, List<OrderTable> orderTables) {
        if (orderTables.size() != createTableGroupRequest.getOrderTables().size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.existsTableGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private List<OrderTable> findOrderTables(List<TableInfo> tableGroupOrderTablesRequest) {
        final List<Long> orderTableIds = tableGroupOrderTablesRequest.stream()
                .map(TableInfo::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateOrder(orderTables);

        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateOrder(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}

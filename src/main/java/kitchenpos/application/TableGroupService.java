package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.OrderTableRequest;
import kitchenpos.application.dtos.TableGroupRequest;
import kitchenpos.application.dtos.TableGroupResponse;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
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
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> orderTablesValue = request.getOrderTables().stream()
                .map(it -> OrderTable.builder()
                        .id(it.getId())
                        .build())
                .collect(Collectors.toList());
        final OrderTables orderTables = new OrderTables(orderTablesValue);
        final List<Long> orderTableIds = request.getOrderTables().stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        final TableGroup tableGroup = TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .build();

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        savedOrderTables.checkValidity(orderTables);
        savedOrderTables.update(savedTableGroup.getId(), false);

        return new TableGroupResponse(savedTableGroup, savedOrderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }
}

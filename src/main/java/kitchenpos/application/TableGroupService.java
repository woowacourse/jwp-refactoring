package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableGroupingSizeException;
import kitchenpos.exception.OrderTableUnableUngroupingStatusException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest tableGroupCreateRequest) {
        validateGroupingSize(tableGroupCreateRequest.getOrderTables());

        List<OrderTable> orderTables = findAllOrderTablesBy(tableGroupCreateRequest.getOrderTables());
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        groupingOrderTables(orderTables, tableGroup);

        return tableGroup;
    }

    private void validateGroupingSize(List<OrderTableIdDto> orderTableIdDtos) {
        if (CollectionUtils.isEmpty(orderTableIdDtos) || orderTableIdDtos.size() < 2) {
            throw new OrderTableGroupingSizeException();
        }
    }

    private List<OrderTable> findAllOrderTablesBy(List<OrderTableIdDto> orderTableIdDtos) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
                orderTableIdDtos.stream()
                        .map(OrderTableIdDto::getId)
                        .collect(Collectors.toList())
        );
        validateOrderTablesSize(orderTableIdDtos, orderTables);

        return orderTables;
    }

    private void validateOrderTablesSize(List<OrderTableIdDto> orderTableIdDtos,
                                         List<OrderTable> orderTables) {
        if (orderTableIdDtos.size() != orderTables.size()) {
            throw new NotFoundOrderTableException();
        }
    }

    private void groupingOrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.grouping(tableGroup);
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderTablesStatus(orderTables);

        ungroupingOrderTables(orderTables);
    }

    private void validateOrderTablesStatus(List<OrderTable> orderTables) {
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderTableUnableUngroupingStatusException();
        }
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void ungroupingOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungrouping();
        }
    }
}

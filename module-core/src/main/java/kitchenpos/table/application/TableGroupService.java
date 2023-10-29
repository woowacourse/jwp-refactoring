package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertablegroup.OrderTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.tablegroup.OrderTableIdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final OrderTableValidator orderValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public TableGroup create(final List<OrderTableIdRequest> orderTableIdRequests) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList()));

        validateOrderTableStatus(orderTables);

        return createTableGroupBy(orderTables);
    }

    private void validateOrderTableStatus(final List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException("단체 지정시 주문 테이블은 비어있을 수 없습니다.");
        }
    }

    private TableGroup createTableGroupBy(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.update(tableGroup, false);
            orderTableRepository.save(savedOrderTable);
        }

        tableGroup.updateOrderTables(orderTables);
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderValidator.validateOrderStatusByOrderTableIds(orderTableIds);
        unGroupOrderTable(orderTables);
    }


    private void unGroupOrderTable(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
            orderTableRepository.save(orderTable);
        }
    }
}

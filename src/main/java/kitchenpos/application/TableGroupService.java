package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> requestOrderTableIds = request.getOrderTableIds();
        if (CollectionUtils.isEmpty(requestOrderTableIds) || requestOrderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(requestOrderTableIds);

        if (requestOrderTableIds.size() != findOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : findOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup newTableGroup = TableGroup.emptyOrderTables();
        newTableGroup.addOrderTablesAndChangeEmptyFull(new OrderTables(findOrderTables));

        return tableGroupRepository.save(newTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        orderRepository.findOrdersByTableGroupId(tableGroupId)
                .forEach(Order::ungroupOrderTable);
    }
}

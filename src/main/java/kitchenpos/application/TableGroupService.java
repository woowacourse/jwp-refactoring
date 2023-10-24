package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public Long create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getTableIds();

        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        orderTables.checkTableSizeIsEqual(orderTableIds.size());
        orderTables.checkEmptyAndTableGroups();

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        for (final OrderTable orderTable : orderTables.getTables()) {
            orderTable.addGroup(savedTableGroup);
            orderTableRepository.save(orderTable);
            savedTableGroup.addOrderTable(orderTable);
        }

        return savedTableGroup.getId();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        final List<Long> orderTableIds = orderTables.getTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 완료되지 않았습니다.");
        }

        for (final OrderTable orderTable : orderTables.getTables()) {
            orderTable.unGroup();
            orderTableRepository.save(orderTable);
        }
    }
}

package kitchenpos.tablegroup.application;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.TableGroupCreateResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final TableGroupValidator tableGroupValidator,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroupCreateResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();

        final TableGroup tableGroup = TableGroup.of(orderTableIds, tableGroupValidator);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final List<OrderTable> orderTables = findOrderTablesByIds(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.makeTableGroup(savedTableGroup.getId());
            orderTableRepository.save(orderTable);
        }

        return TableGroupCreateResponse.of(savedTableGroup, orderTables);
    }

    private List<OrderTable> findOrderTablesByIds(final List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupById(tableGroupId);
        final List<OrderTable> findOrderTables = findOrderTablesByTableGroup(tableGroup);

        checkCompletedOrder(findOrderTables);

        final OrderTables orderTables = new OrderTables(findOrderTables);
        orderTables.ungroup();
    }

    private TableGroup findByTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다. id = " + tableGroupId));
    }

    private List<OrderTable> findOrderTablesByTableGroup(final TableGroup tableGroup) {
        return orderTableRepository.findAllByTableGroupId(tableGroup.getId());
    }

    private void checkCompletedOrder(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }
    }
}

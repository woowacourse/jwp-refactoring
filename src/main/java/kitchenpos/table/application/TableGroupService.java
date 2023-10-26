package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.dto.table.TableGroupRequest;
import kitchenpos.table.application.dto.table.TableGroupResponse;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
        final OrderTables orderTables = getOrderTables(request.getOrderTables());
        final TableGroup tableGroup = TableGroup.forSave();
        tableGroupRepository.save(tableGroup);
        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            tableGroup.addOrderTable(orderTable);
        }
        return TableGroupResponse.from(tableGroup);
    }

    private OrderTables getOrderTables(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("실제 존재하지 않은 주문 테이블이 포함되어 있습니다.");
        }
        return new OrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        validateOrderTableIsCompletion(orderTables);
        tableGroup.ungroup();
    }

    private void validateOrderTableIsCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("현재 요리중이거나 식사 중인 경우 그룹해제를 할 수 없습니다.");
        }
    }
}

package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        OrderTables orderTables = OrderTables.of(savedOrderTables, orderTableIds);

        TableGroup tableGroup = request.toEntity();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        orderTables.groupTables(savedTableGroup);

        return TableGroupResponse.of(tableGroup, orderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        checkStatusForUngrouping(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void checkStatusForUngrouping(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 상태가 완료된 상태여야 그룹 해제를 할 수 있습니다.");
        }
    }
}

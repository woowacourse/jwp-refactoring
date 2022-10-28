package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tableGroup.CreateTableGroupRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private static final List<String> ORDER_STATUS_FOR_CANT_UNGROUP = new ArrayList<String>() {{
        add(OrderStatus.COOKING.name());
        add(OrderStatus.MEAL.name());
    }};

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTables = request.getOrderTables().stream()
            .map(table -> findOrderTableById(table.getId()))
            .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup(orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        validateAbleToUngroup(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableRepository.save(orderTable);
        }
    }

    private OrderTable findOrderTableById(final Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));
    }

    private void validateAbleToUngroup(final List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, ORDER_STATUS_FOR_CANT_UNGROUP)) {
            throw new IllegalArgumentException("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }
    }
}

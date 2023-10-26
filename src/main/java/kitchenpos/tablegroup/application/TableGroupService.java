package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
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
        final TableGroup tableGroup = TableGroup.forSave();
        final OrderTables orderTables = getOrderTables(request.getOrderTables());
        orderTables.join(tableGroup);
        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(tableGroup, orderTables);
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
        final OrderTables orderTables = new OrderTables(orderTableRepository.findByTableGroup(tableGroup));
        validateOrderTableIsCompletion(orderTables);
        orderTables.ungroup();
    }

    private void validateOrderTableIsCompletion(final OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("현재 요리중이거나 식사 중인 경우 그룹해제를 할 수 없습니다.");
        }
    }
}

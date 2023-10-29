package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableIdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream().map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
        final TableGroup tableGroup = TableGroup.forSave();
        final List<OrderTable> savedOrderTables = orderTableRepository.getAllById(orderTableIds);
        validateEmptyOrderTables(savedOrderTables);
        final OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.group(tableGroup.getId());

        return TableGroupResponse.of(tableGroup, orderTables);
    }

    private void validateEmptyOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 없습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        final List<OrderTable> orderTableGroupedByTableGroup = orderTableRepository.findByTableGroupId(
            tableGroup.getId());
        validateProceedingTable(orderTableGroupedByTableGroup);

        orderTableGroupedByTableGroup.forEach(OrderTable::ungroup);
    }

    private void validateProceedingTable(final List<OrderTable> orderTableGroupedByTableGroup) {
        if (orderTableGroupedByTableGroup.stream()
            .anyMatch(this::hasProceedingOrder)) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 테이블 그룹을 해제할 수 없습니다.");
        }
    }

    public boolean hasProceedingOrder(final OrderTable orderTable) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        return orders.stream()
            .anyMatch(Order::isProceeding);
    }
}

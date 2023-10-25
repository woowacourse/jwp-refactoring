package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            OrderRepository orderRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest request) {
        List<OrderTable> orderTables = findOrderTablesByRequest(request.getOrderTableIds());
        TableGroup tableGroup = TableGroup.from();
        Long tableGroupId = tableGroupRepository.save(tableGroup)
                .getId();
        orderTables.forEach(orderTable -> orderTable.registerTableGroup(tableGroupId));

        return tableGroup;
    }

    private List<OrderTable> findOrderTablesByRequest(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("주문 그룹을 구성하기 위해서는 주문 테이블이 최소 2개 있어야 합니다.");
        }

        return orderTableIds.stream()
                .map(this::findOrderTable)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        validateOrderTableId(orderTableId);

        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableId(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        validateTableGroupId(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(this::ungroupOrderTable);
    }

    private void ungroupOrderTable(OrderTable orderTable) {
        validateOrderStatusInOrderTable(orderTable);
        orderTable.breakupTableGroup();
    }

    private void validateOrderStatusInOrderTable(OrderTable orderTable) {
        boolean canNotBreakup = orderRepository.findAllByOrderTableId(orderTable.getId())
                .stream()
                .anyMatch(order -> order.isCooking() || order.isMeal());

        if (canNotBreakup) {
            throw new IllegalArgumentException("요리 중이거나 식사 중인 주문이 포함된 주문 테이블을 테이블 그룹에서 제외 시킬 수 없습니다.");
        }
    }

    private void validateTableGroupId(Long tableGroupId) {
        if (Objects.isNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블 그룹의 ID 는 존재하지 않을 수 없습니다.");
        }

        if (!tableGroupRepository.existsById(tableGroupId)) {
            throw new IllegalArgumentException("테이블 그룹이 존재하지 않으면 안됩니다.");
        }
    }

}

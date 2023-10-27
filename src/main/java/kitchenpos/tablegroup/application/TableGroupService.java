package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
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
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTableRequest> orderTableRequests = request.getOrderTables();

        if (orderTableRequests.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }

        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableRequests.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("요청한 주문 테이블 수와 저장된 주문 테이블의 수가 다릅니다.");
        }

        final TableGroup newTableGroup = new TableGroup(savedOrderTables);
        return tableGroupRepository.save(newTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateNotCompletion(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateNotCompletion(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("결제가 완료되지 않은 테이블이 존재합니다.");
        }
    }
}

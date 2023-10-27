package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupGenerator;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupGenerator tableGroupGenerator;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final TableGroupGenerator tableGroupGenerator
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupGenerator = tableGroupGenerator;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTableRequest> orderTableRequests = request.getOrderTables();
        final List<Long> orderTableIds = getOrderTableIds(orderTableRequests);

        final TableGroup newTableGroup = TableGroup.of(tableGroupGenerator, orderTableIds);
        final TableGroup savedTableGroup = tableGroupRepository.save(newTableGroup);
        tableGroupGenerator.setOrderTableGroupId(savedTableGroup.getId(), orderTableIds);
        return savedTableGroup;
    }

    private List<Long> getOrderTableIds(final List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
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

package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private static final List<String> INCLUDE_ORDER_STATUS_NAME
            = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = findAllOrderTablesByIdIn(request);

        final TableGroup tableGroup = TableGroup.create();
        tableGroupRepository.save(tableGroup);
        tableGroup.appendOrderTables(orderTables);

        return TableGroupResponse.toResponse(tableGroup);
    }

    private List<OrderTable> findAllOrderTablesByIdIn(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("[ERROR] 저장된 데이터의 수와 실제 주문 테이블의 수가 다릅니다.");
        }

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateAllOrderComplement(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateAllOrderComplement(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, INCLUDE_ORDER_STATUS_NAME)) {
            throw new IllegalArgumentException("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }
}

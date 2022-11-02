package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.request.TableForGroupingRequest;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<TableForGroupingRequest> orderTableRequests = request.getOrderTables();
        validateTableGroupSize(orderTableRequests);

        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(TableForGroupingRequest::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateExistTables(orderTableRequests, savedOrderTables);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedOrderTables.forEach(orderTable -> orderTable.group(savedTableGroup));

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("식사가 완료되지 않았습니다.");
        }

        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateTableGroupSize(final List<TableForGroupingRequest> orderTableRequests) {
        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException("2개 이상의 테이블만 단체 지정이 가능합니다.");
        }
    }

    private void validateExistTables(final List<TableForGroupingRequest> orderTableRequests,
                                     final List<OrderTable> savedOrderTables) {
        if (orderTableRequests.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블은 단체 지정할 수 없습니다.");
        }
    }
}

package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.mapper.TableGroupMapper;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.SingleOrderTableCreateRequest;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public TableGroupResponse create(
            final TableGroupCreateRequest tableGroupCreateRequest
    ) {
        final TableGroup tableGroup = saveTableGroup(tableGroupCreateRequest);

        return TableGroupMapper.toTableGroupResponse(tableGroup);
    }

    private TableGroup saveTableGroup(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<OrderTable> orderTables = convertToOrderTables(tableGroupCreateRequest.getOrderTables());
        final TableGroup tableGroup = TableGroupMapper.toTableGroup(LocalDateTime.now(), orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> convertToOrderTables(List<SingleOrderTableCreateRequest> requests) {
        return requests.stream()
                .map(this::convertToOrderTable)
                .collect(Collectors.toList());
    }

    private OrderTable convertToOrderTable(final SingleOrderTableCreateRequest request) {
        return orderTableRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 order table 입니다."));
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 table group 입니다."));
        validateOrderStatus(tableGroup);

        tableGroup.ungroup();
    }

    private void validateOrderStatus(final TableGroup tableGroup) {
        final List<OrderStatus> statuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, statuses)) {
            throw new IllegalArgumentException("주문 상태가 MEAL, COOKING 이면 그룹을 해제할 수 없습니다.");
        }
    }
}

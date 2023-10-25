package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private static final List<String> INCLUDE_ORDER_STATUS_NAMES
            = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final TableGroup findTableGroup = findTableGroup(request.getTableGroupId());
        final OrderTable orderTable = new OrderTable(findTableGroup, request.getNumberOfGuests(), request.isEmpty());
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    private TableGroup findTableGroup(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 테이블 그룹입니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable findOrderTable = findOrderTable(orderTableId);
        validateOrderStatusComplete(orderTableId);
        findOrderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.from(findOrderTable);
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 주문 테이블입니다."));
    }

    private void validateOrderStatusComplete(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, INCLUDE_ORDER_STATUS_NAMES)) {
            throw new IllegalArgumentException("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberRequest request) {
        final OrderTable findOrderTable = findOrderTable(orderTableId);
        findOrderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(findOrderTable);
    }
}

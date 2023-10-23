package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private static final List<String> INCLUDE_ORDER_STATUS_NAMES
            = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(null, request.getNumberOfGuests(), request.isEmpty());
        orderTableRepository.save(orderTable);
        return OrderTableResponse.toResponse(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::toResponse)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable findOrderTable = findOrderTable(orderTableId);
        validateOrderStatusComplete(orderTableId);
        findOrderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.toResponse(findOrderTable);
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
        return OrderTableResponse.toResponse(findOrderTable);
    }
}

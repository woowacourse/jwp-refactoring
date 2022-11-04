package kitchenpos.application.table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.request.ChangeGuestNumberRequest;
import kitchenpos.dto.order.request.EmptyOrderTableRequest;
import kitchenpos.dto.table.request.OrderTableCreateRequest;
import kitchenpos.dto.table.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(request.getNumberOfGuests())
                .empty(request.isEmpty())
                .build();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final EmptyOrderTableRequest request) {
        OrderTable orderTable = getOrderTable(orderTableId);
        checkOrderTableStatus(orderTableId);
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final ChangeGuestNumberRequest request) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuest(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    private void checkOrderTableStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 주문 테이블은 비활성화할 수 없습니다.");
        }
    }
}

package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderTableCreateRequest;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.order.application.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.order.application.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = OrderTable.forSave(request.getNumberOfGuests(), request.isEmpty());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        validateEmptyOrderTable(request.getEmpty(), orderTable);
        orderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTable);
    }

    private void validateEmptyOrderTable(final boolean empty, final OrderTable orderTable) {
        final List<Order> savedOrders = orderRepository.findAllByOrderTableId(orderTable.getId());
        final Orders orders = new Orders(savedOrders, orderTable);
        if (empty && orders.hasProceedingOrder()) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 빈 테이블로 설정할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableUpdateNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        validateNumberOfGuests(orderTable);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }

    private void validateNumberOfGuests(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
    }
}

package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.presentation.dto.ChangeEmptyRequest;
import kitchenpos.order.presentation.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.order.presentation.dto.CreateOrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    private final OrderRepository orderRepository;

    public OrderTableService(final OrderTableRepository orderTableRepository,
                             final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTable create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(null,
                                                     request.getNumberOfGuests(),
                                                     request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId,
                                  final ChangeEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                          .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("이미 그룹화된 주문 테이블의 상태를 변경할 수 없습니다.");
        }
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        final boolean isOrderStillInProgress = orders.stream()
                                                     .anyMatch(order -> !order.isCompleted());
        if (isOrderStillInProgress) {
            throw new IllegalArgumentException("진행중인 주문이 있어, 주문 테이블의 상태를 변경할 수 없습니다.");
        }
        final boolean empty = request.isEmpty();
        orderTable.changeEmpty(empty);
        return orderTable;
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final ChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                          .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 상태의 주문 테이블에서는 방문 손님 수를 변경할 수 없습니다.");
        }
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTable;
    }
}

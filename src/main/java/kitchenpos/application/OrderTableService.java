package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.KitchenPosException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(
            null,
            request.isEmpty(),
            request.getNumberOfGuests()
        ));
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::from)
            .collect(toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, boolean empty) {
        OrderTable orderTable = findOrderTable(orderTableId);
        Order order = findOrderByOrderTableId(orderTableId);
        if (order.getOrderStatus() != OrderStatus.COMPLETION) {
            throw new KitchenPosException("계산 완료 상태가 아닌 주문이 있는 테이블은 상태를 변경할 수 없습니다. orderTableId=" + orderTableId);
        }
        orderTable.changeEmpty(empty);
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new KitchenPosException("해당 주문 테이블이 없습니다. orderTableId=" + orderTableId));
    }

    private Order findOrderByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId)
            .orElseThrow(() -> new KitchenPosException("해당 주문이 없습니다. orderTableId=" + orderTableId));
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTable);
    }
}

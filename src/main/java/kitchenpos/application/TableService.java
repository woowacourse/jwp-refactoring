package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(OrderTableRepository orderTableRepository,
                        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable create() {
        return orderTableRepository.save(new OrderTable());
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        //
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        validateNoOngoing(orders);
        //

        if (empty) {
            orderTable.empty();
        }
        if (!empty) {
            orderTable.fill();
        }
        return orderTable;
    }

    //
    private void validateNoOngoing(List<Order> orders) {
        if (hasAnyOngoing(orders)) {
            throw new IllegalArgumentException("이미 진행중인 주문이 있습니다");
        }
    }

    private boolean hasAnyOngoing(List<Order> orders) {
        return orders.stream()
                .anyMatch(Order::isOngoing);
    }
    //

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        //
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        validateNoOngoing(orders);
        //
        orderTable.fill(numberOfGuests);

        return orderTable;
    }
}

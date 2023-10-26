package kitchenpos.ordertable.service;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.infra.OrderTableRepository;
import kitchenpos.vo.NumberOfGuests;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(null, new NumberOfGuests(numberOfGuests), empty);
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    // TODO: 패키지 양방향 의존 발생
    @Transactional
    public OrderTable changeEmpty(Long orderTableId, boolean empty) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        Order order = orderRepository.getByOrderTable(orderTable);
        if (order.orderStatus() == OrderStatus.COMPLETION) {
            throw new IllegalStateException("계산 완료된 주문의 테이블의 주문 가능 상태를 변경할 수 없다.");
        }
        orderTable.changeEmpty(empty);

        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(new NumberOfGuests(numberOfGuests));
        return orderTable;
    }
}

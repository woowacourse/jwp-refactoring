package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.OrderTable;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.order.persistence.OrderTableRepository;
import kitchenpos.order.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest request) {
        return orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.getEmpty()));
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, boolean changedStatus) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        validateIsCompletionOrder(orderTableId);
        savedOrderTable.changeEmpty(changedStatus);
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문테이블입니다."));
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableRepository.save(savedOrderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    private void validateIsCompletionOrder(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            List.of(OrderStatus.MEAL, OrderStatus.COOKING))) {
            throw new IllegalArgumentException("주문이 완료된 테이블만 상태를 변경할 수 있습니다.");
        }
    }
}

package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable orderTable = getById(orderTableId);
        validateNotCompletedOrderExist(orderTableId);
        orderTable.changeEmpty(empty);
        return OrderTableResponse.from(orderTable);
    }

    private void validateNotCompletedOrderExist(Long orderTableId) {
        if (existNotCompletedOrder(orderTableId)) {
            throw new IllegalArgumentException("주문이 완료되지 않아 상태를 변경할 수 없습니다.");
        }
    }

    private boolean existNotCompletedOrder(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, OrderStatus.listInProgress()
        );
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {
        OrderTable orderTable = getById(orderTableId);
        orderTable.updateNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }
}

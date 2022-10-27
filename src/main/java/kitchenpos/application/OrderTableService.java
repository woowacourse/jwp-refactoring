package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(OrderTableRepository orderTableRepository,
        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        validOrderTable(orderTable);

        orderTable.changeEmpty(request);

        return orderTable;
    }

    private void validOrderTable(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(
            orderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        orderTable.updateNumberOfGuests(numberOfGuests);

        return orderTable;
    }
}

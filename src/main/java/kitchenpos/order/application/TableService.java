package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        orderTable.changeEmptyStatus(request.isEmpty());
        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }
}

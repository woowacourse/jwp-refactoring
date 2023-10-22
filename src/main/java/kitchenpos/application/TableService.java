package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.application.dto.request.UpdateOrderTableGuests;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final CreateOrderTableRequest createOrderTableRequest) {
        OrderTable orderTable = new OrderTable(createOrderTableRequest.getNumberOfGuests(), createOrderTableRequest.isEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest updateOrderTableEmptyRequest) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validateIsOrderNotCompleted(orderTableId);
        orderTable.changeEmpty(updateOrderTableEmptyRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateIsOrderNotCompleted(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, UpdateOrderTableGuests updateOrderTableGuests) {
        int numberOfGuests = updateOrderTableGuests.getNumberOfGuests();
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }
}

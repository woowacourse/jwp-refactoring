package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> readAll() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse updateIsEmpty(Long orderTableId, OrderTableUpdateEmptyRequest request) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);

        validateStatusWhenUpdateEmptyToTrue(request, orderTable);

        orderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.from(orderTable);
    }

    private void validateStatusWhenUpdateEmptyToTrue(OrderTableUpdateEmptyRequest request, OrderTable orderTable) {
        if (request.isEmpty()) {
            orderTable.validateTableGroupIsNull();

            Optional<Order> orderOfOrderTable = orderRepository.findByOrderTable(orderTable);
            orderOfOrderTable.ifPresent(Order::validateOrderStatusIsCompletion);
        }
    }

    @Transactional
    public OrderTableResponse updateNumberOfGuests(Long orderTableId, OrderTableUpdateNumberOfGuestsRequest request) {
        int numberOfGuests = request.getNumberOfGuests();
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }
}

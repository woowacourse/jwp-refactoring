package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.ChangeEmptyTableRequest;
import kitchenpos.dto.request.ChangeTableGuestRequest;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(CreateOrderTableRequest request) {
        OrderTable orderTable = new OrderTable(null, request.getNumberOfGuest(), request.getEmpty());
        orderTableRepository.save(orderTable);

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> findAll() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, ChangeEmptyTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        validateOrdersCompleted(orderTable);
        orderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTable);
    }

    private void validateOrdersCompleted(OrderTable orderTable) {
        List<Order> orders = orderRepository.findByOrderTable(orderTable);
        for (Order order : orders) {
            order.validateOrderIsCompleted();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, ChangeTableGuestRequest request) {
        int numberOfGuests = request.getNumberOfGuests();

        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }
}

package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.table.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            ApplicationEventPublisher publisher
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
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

        orderTable.changeEmpty(request.isEmpty());
        orderTableRepository.save(orderTable.publish());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse updateNumberOfGuests(Long orderTableId, OrderTableUpdateNumberOfGuestsRequest request) {
        int numberOfGuests = request.getNumberOfGuests();
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }
}

package kitchenpos.table.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.event.ValidateAllOrderCompletedEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.ChangeEmptyTableRequest;
import kitchenpos.table.dto.request.ChangeTableGuestRequest;
import kitchenpos.table.dto.request.CreateOrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final ApplicationEventPublisher eventPublisher;
    private final OrderTableRepository orderTableRepository;

    public TableService(ApplicationEventPublisher eventPublisher, OrderTableRepository orderTableRepository) {
        this.eventPublisher = eventPublisher;
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

        eventPublisher.publishEvent(new ValidateAllOrderCompletedEvent(orderTable.getId()));
        orderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTable);
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

package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderStatusValidateEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableIsEmptyUpdateRequest;
import kitchenpos.table.dto.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toOrderTableWithoutGroup());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeIsEmpty(Long orderTableId, OrderTableIsEmptyUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        publisher.publishEvent(new OrderStatusValidateEvent(orderTable.getId()));

        orderTable.changeIsEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableNumberOfGuestsUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}

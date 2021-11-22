package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrdersValidatedEvent;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableService(final OrderTableRepository orderTableRepository,
                        final ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = new OrderTable(
                null,
                orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty()
        );
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.toList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateGroup();

        applicationEventPublisher.publishEvent(new OrdersValidatedEvent(savedOrderTable.getId()));

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateEmpty();

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(savedOrderTable);
    }
}

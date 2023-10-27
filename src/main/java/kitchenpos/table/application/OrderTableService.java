package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.presentation.dto.OrderTableCreateRequest;
import kitchenpos.table.presentation.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.table.presentation.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public OrderTableService(final OrderTableRepository orderTableRepository,
                             final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableRepository.save(new OrderTable(request.getNumberOfGuest(), request.isEmpty()));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAll() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId,
                                  final OrderTableUpdateEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        if (request.isEmpty()) {
            orderTable.validateChangeEmpty();
            publisher.publishEvent(new OrderTableChangeEmptyEvent(orderTable.getId()));
        }

        orderTable.changeEmpty(request.isEmpty());
        return orderTable;
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableUpdateNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return orderTable;
    }
}
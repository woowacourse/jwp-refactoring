package kitchenpos.ordertable;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final JpaOrderTableRepository jpaOrderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableService(
            JpaOrderTableRepository jpaOrderTableRepository,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.jpaOrderTableRepository = jpaOrderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable savedOrderTable = jpaOrderTableRepository.save(orderTable);
        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = jpaOrderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = jpaOrderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        applicationEventPublisher.publishEvent(new OrderTableEvent(savedOrderTable.getId()));

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return new OrderTableResponse(savedOrderTable);
    }


    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = jpaOrderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.setNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(savedOrderTable);
    }
}

package kitchenpos.table.application;

import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableChangeEmptyValidateOrderStatusEvent;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableService(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public Long create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(orderTable).getId();
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        final List<OrderTableResponse> orderTableResponses = new ArrayList<>();
        for (final OrderTable orderTable : orderTables) {
            orderTableResponses.add(OrderTableResponse.from(orderTable));
        }

        return orderTableResponses;
    }

    @Transactional
    public Long changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (!savedOrderTable.isTableGroupNull()) {
            throw new IllegalArgumentException();
        }

        applicationEventPublisher.publishEvent(new OrderTableChangeEmptyValidateOrderStatusEvent(savedOrderTable.getId()));

        savedOrderTable.changeEmpty(empty);

        return orderTableRepository.save(savedOrderTable).getId();
    }

    @Transactional
    public Long changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable).getId();
    }
}

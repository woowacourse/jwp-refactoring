package kitchenpos.table.application;

import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableEmptyModifyRequest;
import kitchenpos.table.application.dto.request.OrderTableNumberOfGuestModifyRequest;
import kitchenpos.table.application.dto.response.OrderTableQueryResponse;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.ValidateOrderOfTableEvent;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final ApplicationEventPublisher publisher;
    private final OrderTableRepository orderTableRepository;

    public TableService(final ApplicationEventPublisher publisher,
                        final OrderTableRepository orderTableRepository) {
        this.publisher = publisher;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableQueryResponse create(final OrderTableCreateRequest request) {
        final OrderTable savedOrderTable = request.toOrderTable();

        return OrderTableQueryResponse.from(orderTableRepository.save(savedOrderTable));
    }

    public List<OrderTableQueryResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableQueryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableQueryResponse changeEmpty(final Long orderTableId,
                                               final OrderTableEmptyModifyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        publisher.publishEvent(new ValidateOrderOfTableEvent(orderTableId));
        savedOrderTable.updateEmpty(request.isEmpty());

        return OrderTableQueryResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableQueryResponse changeNumberOfGuests(final Long orderTableId,
                                                        final OrderTableNumberOfGuestModifyRequest request) {
        final NumberOfGuests numberOfGuests = NumberOfGuests.from(request.getNumberOfGuests());
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableQueryResponse.from(orderTableRepository.save(savedOrderTable));
    }
}

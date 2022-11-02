package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.event.VerifiedAbleToChangeEmptyEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.table.dto.request.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.table.dto.request.CreateOrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableService(OrderTableRepository orderTableRepository, ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderTableResponse create(final CreateOrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(
            new OrderTable(request.getNumberOfGuests(), request.isEmpty()));

        return new OrderTableResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        publisher.publishEvent(new VerifiedAbleToChangeEmptyEvent(orderTable.getId()));

        orderTable.changeEmpty(request.isEmpty());

        return new OrderTableResponse(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final ChangeOrderTableNumberOfGuestRequest request) {

        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeNumberOfGuests(numberOfGuests);

        return new OrderTableResponse(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));
    }

}

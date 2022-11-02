package kitchenpos.table.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.event.VerifiedAbleToChangeEmptyEvent;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.table.dto.request.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.table.dto.request.CreateOrderTableRequest;

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
    public OrderTable create(final CreateOrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        publisher.publishEvent(new VerifiedAbleToChangeEmptyEvent(orderTable.getId()));

        orderTable.changeEmpty(request.isEmpty());
        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
        final ChangeOrderTableNumberOfGuestRequest request) {

        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));
    }

}

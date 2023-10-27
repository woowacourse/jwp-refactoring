package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.application.dto.event.OrderTableChangeEmptyEvent;
import kitchenpos.table.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        applicationEventPublisher.publishEvent(new OrderTableChangeEmptyEvent(orderTableId));
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }
}

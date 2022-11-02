package kitchenpos.table.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.event.ChangeEmptyEvent;
import kitchenpos.table.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderTableService(OrderTableRepository orderTableRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean request) {
        OrderTable orderTable = getOrderTable(orderTableId);

        applicationEventPublisher.publishEvent(new ChangeEmptyEvent(orderTable));

        orderTable.changeEmpty(request);

        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable orderTable = getOrderTable(orderTableId);

        orderTable.updateNumberOfGuests(numberOfGuests);

        return orderTable;
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}

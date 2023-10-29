package kitchenpos.application;

import kitchenpos.application.dto.OrderTableChangeGuestRequest;
import kitchenpos.application.dto.OrderTableChangeStatusRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.repository.OrderTableRepository;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatusCheckEvent;
import kitchenpos.domain.OrderTable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableService(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeStatusRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        savedOrderTable.validateOrderTableHasTableGroupId();

        publisher.publishEvent(new OrderStatusCheckEvent(orderTableId));
        savedOrderTable.changeEmptyStatus(request.isEmpty());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeGuestRequest orderTableChangeGuestRequest
    ) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        orderTable.changeNumberOfGuests(orderTableChangeGuestRequest.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}

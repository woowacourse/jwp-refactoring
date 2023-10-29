package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderStatusValidated;
import kitchenpos.table.dto.OrderTableChangNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final ApplicationEventPublisher applicationEventPublisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(null, request.getNumberOfGuests(), request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTableBy(orderTableId);

        applicationEventPublisher.publishEvent(new OrderStatusValidated(orderTableId));

        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableChangNumberOfGuestRequest request) {
        final OrderTable savedOrderTable = findOrderTableBy(orderTableId);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable findOrderTableBy(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }
}

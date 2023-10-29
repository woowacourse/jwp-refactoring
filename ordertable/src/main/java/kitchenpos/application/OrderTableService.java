package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableChangeEmptyEvent;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableUpdateRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public OrderTableService(final OrderTableRepository orderTableRepository,
                             final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.save(request.toEntity());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = findByIdOrThrow(orderTableId);
        orderTable.changeEmpty(request.isEmpty());

        publisher.publishEvent(new OrderTableChangeEmptyEvent(orderTableId));
        return OrderTableResponse.from(orderTable);
    }

    public OrderTable findByIdOrThrow(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = findByIdOrThrow(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTable> findAllByIdsOrThrow(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블입니다.");
        }

        return orderTables;
    }
}

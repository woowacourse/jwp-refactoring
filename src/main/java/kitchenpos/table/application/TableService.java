package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGuestChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTable) {
        OrderTable nullTable = new OrderTable(orderTable.getNumberOfGuests(), orderTable.isEmpty());
        return toOrderTableResponse(orderTableRepository.save(nullTable));
    }

    public List<OrderTableResponse> list() {
        return toOrderTableResponses(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        orderRepository.validateOrdersCompleted(orderTableId);

        final OrderTable orderTable = orderTableRepository.findById(orderTableId);
        orderTable.changeEmpty(orderTableChangeEmptyRequest.isEmpty());
        return toOrderTableResponse(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final TableGuestChangeRequest changeRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId);
        orderTable.validateTableIsFull();
        orderTable.placeNumberOfGuests(changeRequest.getNumberOfGuests());
        return toOrderTableResponse(orderTableRepository.save(orderTable));
    }

    private OrderTableResponse toOrderTableResponse(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    private List<OrderTableResponse> toOrderTableResponses(List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream()
                .map(this::toOrderTableResponse)
                .collect(Collectors.toList());
    }
}

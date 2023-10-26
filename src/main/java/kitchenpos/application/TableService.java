package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ordertable.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.ordertable.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private static final List<String> UNCHANGEABLE_ORDER_STATUSES = List.of(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableRepository.save(createOrderTableByRequest(request));
    }

    private OrderTable createOrderTableByRequest(final OrderTableCreateRequest request) {
        return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        validateOrderTableGroupExist(orderTable);
        validateOrderTableOrderStatuses(orderTable);
        orderTable.changeEmpty(request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableGroupExist(final OrderTable orderTable) {
        if (orderTable.hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableOrderStatuses(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), UNCHANGEABLE_ORDER_STATUSES)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }
}

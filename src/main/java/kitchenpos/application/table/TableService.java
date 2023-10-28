package kitchenpos.application.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.ChangeOrderTableOrderableRequest;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.ListOrderTableResponse;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.exception.order.OrderNotFoundException;
import kitchenpos.exception.order.OrderTableNotFoundException;
import kitchenpos.exception.table.OrderIsNotCompletedBadRequestException;
import kitchenpos.exception.table.OrderTableIsInOtherTableGroupBadRequest;
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
    public OrderTableResponse create(CreateOrderTableRequest reqeust) {
        final OrderTable orderTable = OrderTable.of(reqeust.getNumberOfGuests(), reqeust.isOrderable());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public ListOrderTableResponse list() {
        return ListOrderTableResponse.of(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeOrderable(final Long orderTableId, final ChangeOrderTableOrderableRequest request) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        validateIfOrderTableHasOrder(orderTableId, savedOrderTable);
        validateIfOrderTableIsGrouped(savedOrderTable);
        savedOrderTable.setOrderable(request.isOrderable());
        return OrderTableResponse.of(savedOrderTable);
    }

    private void validateIfOrderTableHasOrder(final Long orderTableId, final OrderTable savedOrderTable) {
        if (orderRepository.existsByOrderTableId(orderTableId)) {
            final Order savedOrder = findOrderByOrderTableId(savedOrderTable);
            validateOrderCompleted(savedOrder, savedOrderTable);
        }
    }

    private static void validateIfOrderTableIsGrouped(final OrderTable savedOrderTable) {
        if (savedOrderTable.isGrouped()) {
            throw new OrderTableIsInOtherTableGroupBadRequest(savedOrderTable.getId());
        }
    }

    private void validateOrderCompleted(final Order order, final OrderTable orderTable) {
        if (order.isNotCompleted()) {
            throw new OrderIsNotCompletedBadRequestException(orderTable.getId());
        }
    }

    private Order findOrderByOrderTableId(final OrderTable savedOrderTable) {
        return orderRepository.findByOrderTableId(savedOrderTable.getId())
                .orElseThrow(OrderNotFoundException::new);
    }

    private OrderTable findOrderTable(final long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }
}

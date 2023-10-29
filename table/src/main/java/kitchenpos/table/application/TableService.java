package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.ChangeOrderTableOrderableRequest;
import kitchenpos.table.dto.CreateOrderTableRequest;
import kitchenpos.table.dto.ListOrderTableResponse;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.OrderIsNotCompletedBadRequestException;
import kitchenpos.table.exception.OrderTableIsInOtherTableGroupBadRequest;

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

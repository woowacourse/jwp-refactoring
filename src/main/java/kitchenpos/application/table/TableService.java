package kitchenpos.application.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.dto.table.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.ChangeOrderTableOrderableRequest;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.ListOrderTableResponse;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.exception.order.OrderNotFoundException;
import kitchenpos.exception.order.OrderTableNotFoundException;
import kitchenpos.exception.table.OrderIsNotCompletedBadRequestException;
import kitchenpos.exception.table.OrderTableIsInTableGroupBadRequest;
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
    public OrderTableResponse changeOrderable(final ChangeOrderTableOrderableRequest request) {
        final OrderTable savedOrderTable = convertToOrderTable(request.getOrderTableId());
        final Order savedOrder = convertToOrder(savedOrderTable);
        checkOrderCompleted(savedOrder, savedOrderTable);
        checkHasTableGroup(savedOrderTable);
        savedOrderTable.setUnOrderable();
        return OrderTableResponse.of(savedOrderTable);
    }

    private static void checkHasTableGroup(final OrderTable savedOrderTable) {
        if (savedOrderTable.getTableGroup().isPresent()) {
            throw new OrderTableIsInTableGroupBadRequest(savedOrderTable.getId());
        }
    }

    private void checkOrderCompleted(final Order order, final OrderTable orderTable) {
        if (order.isNotCompleted()) {
            throw new OrderIsNotCompletedBadRequestException(orderTable.getId());
        }
    }

    private Order convertToOrder(final OrderTable savedOrderTable) {
        return orderRepository.findByOrderTableId(savedOrderTable.getId())
                .orElseThrow(OrderNotFoundException::new);
    }

    private OrderTable convertToOrderTable(final long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final ChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = convertToOrderTable(request.getOrderTableId());
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }
}

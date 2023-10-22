package kitchenpos.application.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableCreationRequest;
import kitchenpos.application.dto.OrderTableEmptyStatusChangeRequest;
import kitchenpos.application.dto.OrderTableGuestAmountChangeRequest;
import kitchenpos.application.dto.result.OrderTableResult;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.dao.table.OrderTableRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResult create(final OrderTableCreationRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResult> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResult::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResult changeEmpty(final Long orderTableId, final OrderTableEmptyStatusChangeRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        validateOrdersStatus(orders);
        orderTable.changeEmpty(request.getEmpty());
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }

    private void validateOrdersStatus(final List<Order> orders) {
        if (orders.stream().allMatch(Order::isCompleted)) {
            return;
        }
        throw new IllegalArgumentException("Cannot change empty status of table with order status not completion");
    }

    @Transactional
    public OrderTableResult changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableGuestAmountChangeRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }
}

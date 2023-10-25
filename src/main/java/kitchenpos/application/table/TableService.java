package kitchenpos.application.table;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.orderTableException.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(null, orderTableRequest.getNumberOfGuest(), orderTableRequest.getEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        orderTable.changeEmptyStatus(orderTableRequest.getEmpty());
        validateOrderTableStatus(orderTable);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    private void validateOrderTableStatus(final OrderTable orderTable) {
        orderRepository.findAllByOrderTableId(orderTable.getId())
                .forEach(Order::validateOrderComplete);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        final OrderTable newOrderTable = new OrderTable(orderTable.getTableGroupId(), orderTableRequest.getNumberOfGuest(), orderTableRequest.getEmpty());

        final OrderTable savedOrder = orderTableRepository.save(newOrderTable);

        return OrderTableResponse.from(savedOrder);
    }
}

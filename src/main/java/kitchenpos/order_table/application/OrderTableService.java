package kitchenpos.order_table.application;

import java.util.List;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order_table.repository.OrderTableRepository;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.dto.OrderTableRequest;
import kitchenpos.order_table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = OrderTable.builder()
            .empty(request.getEmpty())
            .numberOfGuests(request.getNumberOfGuests())
            .build();
        OrderTable savedTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> tables = orderTableRepository.findAll();
        return OrderTableResponse.listFrom(tables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(
        final Long orderTableId,
        final OrderTableRequest request
    ) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        validateOrderStatus(savedOrderTable);

        savedOrderTable.changeEmpty(request.getEmpty());

        OrderTable changedTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.from(changedTable);
    }

    private void validateOrderStatus(final OrderTable orderTable) {
        if (orderTable.isInProgress()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableRequest request
    ) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        OrderTable changedTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.from(changedTable);
    }
}

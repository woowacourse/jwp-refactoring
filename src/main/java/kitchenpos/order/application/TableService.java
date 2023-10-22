package kitchenpos.order.application;

import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.Empty;
import kitchenpos.ordertable.NumberOfGuests;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.application.OrderTableRepository;
import kitchenpos.ordertable.application.request.ChangeEmptyRequest;
import kitchenpos.ordertable.application.request.NumberOfGuestsRequest;
import kitchenpos.ordertable.application.request.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable create(final OrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(request.getNumberOfGuests()), Empty.from(request.isEmpty()));

        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = validateOrderTable(orderTableId);

        savedOrderTable.updateEmpty(Empty.from(request.isEmpty()));

        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateNumberOfGuests(new NumberOfGuests(numberOfGuests));

        return orderTableRepository.save(savedOrderTable);
    }
}

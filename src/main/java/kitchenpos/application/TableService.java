package kitchenpos.application;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.OrderTableEmptyRequest;
import kitchenpos.ui.request.OrderTableNumberOfGuestRequest;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable newOrderTable = new OrderTable.Builder()
                .id(null)
                .tableGroup(null)
                .numberOfGuests(orderTableRequest.getNumberOfGuests())
                .empty(orderTableRequest.isEmpty())
                .build();

        orderTableRepository.save(newOrderTable);
        return OrderTableResponse.of(newOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAllFetchJoinTableGroup();
        return OrderTableResponse.toList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest orderTableEmptyRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                savedOrderTable, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTableEmptyRequest.getEmpty());

        orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableNumberOfGuestRequest orderTableNumberOfGuestRequest) {
        final int numberOfGuests = orderTableNumberOfGuestRequest.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.of(savedOrderTable);
    }
}

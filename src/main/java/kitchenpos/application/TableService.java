package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.table.OrderTableCreateRequest;
import kitchenpos.application.dto.table.OrderTableCreateResponse;
import kitchenpos.application.dto.table.OrderTableEmptyRequest;
import kitchenpos.application.dto.table.OrderTableNumberOfGuestsRequest;
import kitchenpos.application.dto.table.OrderTableResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableCreateResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableCreateResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(savedOrderTable);
    }
}

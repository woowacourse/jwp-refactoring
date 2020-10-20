package kitchenpos.application;

import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        OrderTable orderTable = orderTableRequest.to();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderTableInGroup(savedOrderTable);

        validateOrderStatus(orderTableId);

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        OrderTable changeEmptyOrderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(changeEmptyOrderTable);
    }

    private void validateOrderTableInGroup(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final NumberOfGuests numberOfGuests = NumberOfGuests.of(orderTableRequest.getNumberOfGuests());

        final OrderTable savedOrderTable = validateOrderTableEmpty(orderTableId);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        OrderTable changeGuestsOrderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(changeGuestsOrderTable);
    }

    private OrderTable validateOrderTableEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmptyTable()) {
            throw new IllegalArgumentException();
        }
        return savedOrderTable;
    }
}

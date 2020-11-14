package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.ordertable.OrderTableChangeRequest;
import kitchenpos.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
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

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = orderTableCreateRequest.toOrderTable();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableChangeRequest orderTableChangeRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTableChangeRequest.isEmpty());
        OrderTable orderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeRequest orderTableCreateRequest) {
        final int numberOfGuests = orderTableCreateRequest.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);
        OrderTable orderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.from(orderTable);
    }
}

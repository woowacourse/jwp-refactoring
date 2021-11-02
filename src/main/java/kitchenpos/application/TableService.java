package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.fromList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        final Optional<Order> unableEmpty = savedOrderTable.getOrders()
            .stream()
            .filter(Order::unableUngroup)
            .findAny();

        if (unableEmpty.isPresent()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(isEmpty);

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(savedOrderTable);
    }
}

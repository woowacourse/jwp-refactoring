package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableEmptyUpdateRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setId(null);
        orderTable.assignTableGroup(null);

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableEmptyUpdateRequest request) {
        final Optional<Order> maybeOrder = orderRepository.findByOrderTableId(orderTableId);
        if (maybeOrder.isPresent()) {
            return changeEmptyByOrder(maybeOrder.get(), request.isEmpty());
        }

        return changeEmptyByOrderTable(orderTableId, request.isEmpty());
    }

    private OrderTable changeEmptyByOrder(final Order findOrder, final boolean isEmpty) {
        findOrder.changeOrderTableEmpty(isEmpty);

        return findOrder.getOrderTable();
    }

    private OrderTable changeEmptyByOrderTable(final Long orderTableId, final boolean isEmpty) {
        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(orderTableId);
        findOrderTable.changeOrderTableEmpty(isEmpty);

        return findOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }
}

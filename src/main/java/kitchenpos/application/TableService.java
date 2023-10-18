package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Long create() {
        return orderTableRepository.save(new OrderTable()).getId();
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public void changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateTableGroupIdIsNull();
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.updateEmpty(isEmpty);
    }

    public void changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateIsNotEmpty();
        savedOrderTable.updateNumberOfGuests(numberOfGuests);
    }
}

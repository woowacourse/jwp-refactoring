package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderTableException.ExistsNotCompletionOrderException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final int numberOfGuests) {
        return orderTableRepository.save(new OrderTable(numberOfGuests));
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new ExistsNotCompletionOrderException(orderTableId);
        }

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        final int numberOfGuests = orderTable.getNumberOfGuests();

        savedOrderTable.changeNumberOfGuest(numberOfGuests);

        return savedOrderTable;
    }
}

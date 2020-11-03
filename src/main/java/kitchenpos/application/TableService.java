package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableDto.of(savedOrderTable);
    }

    public List<OrderTableDto> list() {
        return OrderTableDto.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        if (!orderTableRequest.isEmpty()) {
            changeEmptyWhenFirstOrder(savedOrderTable);
        }
        if (orderTableRequest.isEmpty()) {
            changeEmptyWhenFinishMeal(savedOrderTable);
        }
        return OrderTableDto.of(savedOrderTable);
    }

    private void changeEmptyWhenFirstOrder(OrderTable orderTable) {
        orderTable.changeEmpty(false);
    }

    private void changeEmptyWhenFinishMeal(OrderTable orderTable) {
        List<Order> ordersByOrderTable = orderRepository.findAllByOrderTable(orderTable);
        for (Order order : ordersByOrderTable) {
            order.changeOrderTableEmpty(true);
        }
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableDto.of(savedOrderTable);
    }
}

package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableConvertEmptyStatusException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.getEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request) {
        validateOrderTableStatus(orderTableId);

        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeEmpty(request.getEmpty());

        return orderTable;
    }

    private void validateOrderTableStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderTableConvertEmptyStatusException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, OrderTableChangeNumberOfGuestsRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
    }
}

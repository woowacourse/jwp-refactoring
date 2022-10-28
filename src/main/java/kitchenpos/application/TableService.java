package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.ChangeOrderTableEmptyRequest;
import kitchenpos.dto.table.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class TableService {

    private static final List<String> ORDER_STATUS_FOR_CANT_CHANGE_EMPTY = new ArrayList<String>() {{
        add(OrderStatus.COOKING.name());
        add(OrderStatus.MEAL.name());
    }};

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        validateAbleToChangeEmpty(orderTable);

        orderTable.changeEmpty(request.isEmpty());
        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
        final ChangeOrderTableNumberOfGuestRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));
    }

    private void validateAbleToChangeEmpty(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, ORDER_STATUS_FOR_CANT_CHANGE_EMPTY)) {
            throw new IllegalArgumentException("변경할 수 있는 상태가 아닙니다.");
        }
    }

}

package kitchenpos.ordertable.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.ordertable.application.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.application.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.repository.OrdersValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrdersValidator ordersValidator;

    public TableService(
            OrderTableRepository orderTableRepository,
            OrdersValidator ordersValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.ordersValidator = ordersValidator;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest request) {
        OrderTable orderTable = OrderTable.of(request.getNumberOfGuests(), request.getEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableUpdateEmptyRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);
        ordersValidator.validateOrderStatusInOrderTable(orderTable);
        orderTable.changeEmpty(request.getEmpty());

        return orderTable;
    }

    private OrderTable findOrderTable(Long orderTableId) {
        validateOrderTableId(orderTableId);

        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableId(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, OrderTableUpdateNumberOfGuestsRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }

}

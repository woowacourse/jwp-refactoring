package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.ui.request.OrderTableCreateRequest;
import kitchenpos.ui.request.OrderTableUpdateEmptyRequest;
import kitchenpos.ui.request.OrderTableUpdateNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
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

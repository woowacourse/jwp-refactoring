package kitchenpos.application;

import java.util.List;
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
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, OrderTableUpdateNumberOfGuestsRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }

}

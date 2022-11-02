package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTables;

    public TableService(final OrderTableRepository orderTables) {
        this.orderTables = orderTables;
    }

    @Transactional
    public OrderTable create(final OrderTable request) {
        final var orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return orderTables.add(orderTable);
    }

    public List<OrderTable> list() {
        return orderTables.getAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable request) {
        final OrderTable orderTable = orderTables.get(orderTableId);
        orderTable.changeEmptyTo(request.isEmpty());

        return orderTables.add(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable request) {
        final OrderTable orderTable = orderTables.get(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTables.add(orderTable);
    }
}

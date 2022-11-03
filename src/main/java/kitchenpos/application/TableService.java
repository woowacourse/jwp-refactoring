package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTables;
    private final OrderRepository orders;

    public TableService(final OrderTableValidator orderTableValidator,
                        final OrderTableRepository orderTables,
                        final OrderRepository orders) {
        this.orderTableValidator = orderTableValidator;
        this.orderTables = orderTables;
        this.orders = orders;
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
    public OrderTable changeEmpty(final Long orderTableId, final boolean status) {
        final OrderTable orderTable = orderTables.get(orderTableId);
        final var orders = this.orders.getByOrderTableId(orderTableId);

        orderTableValidator.validateOnChangeEmpty(orderTable, orders);
        orderTable.changeEmptyTo(status);

        return orderTables.add(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int guests) {
        final OrderTable orderTable = orderTables.get(orderTableId);
        orderTable.changeNumberOfGuests(guests);

        return orderTables.add(orderTable);
    }
}

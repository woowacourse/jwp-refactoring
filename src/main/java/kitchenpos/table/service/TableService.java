package kitchenpos.table.service;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.table.dto.request.OrderTableUpdateGuestRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.dto.response.OrderTablesResponse;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(final TableValidator tableValidator,
                        final OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = request.toEntity();
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTablesResponse list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTablesResponse.from(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableUpdateEmptyRequest request) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.validate((it) -> tableValidator.validatePossibleChangeToEmpty((Long) it));
        orderTable.changeToEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableUpdateGuestRequest request) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }
}

package kitchenpos.table.application;

import static kitchenpos.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.CustomIllegalArgumentException;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.domain.JpaOrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.request.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final TableValidator validator;
    private final JpaOrderTableRepository orderTableRepository;

    public TableService(final TableValidator validator, final JpaOrderTableRepository orderTableRepository) {
        this.validator = validator;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.save(request.toOrder());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream().map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        validator.validateChangeStatus(savedOrderTable);
        savedOrderTable.clearTable();
        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));
    }
}

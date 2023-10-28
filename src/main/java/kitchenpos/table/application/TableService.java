package kitchenpos.table.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableCreateResponse;
import kitchenpos.table.application.dto.OrderTableEmptyRequest;
import kitchenpos.table.application.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final OrderTableValidator orderTableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public OrderTableCreateResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableCreateResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeEmpty(request.isEmpty(), orderTableValidator);

        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다. id = " + orderTableId));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(savedOrderTable);
    }
}

package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable orderTable = orderTableCreateRequest.toOrderTable();
        final OrderTable saved = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean changeOrderEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderException::new);

        orderTableValidator.validate(orderTableId);

        savedOrderTable.updateEmpty(changeOrderEmpty);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int changeNumberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);

        savedOrderTable.updateNumberOfGuests(changeNumberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }
}

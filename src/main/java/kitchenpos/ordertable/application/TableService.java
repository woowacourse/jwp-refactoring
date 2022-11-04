package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
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
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = request.toEntity();
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.from(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTableValidator.checkOrderComplete(orderTableId);

        changeTableStatus(request.isEmpty(), savedOrderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        final int numberOfGuests = request.getNumberOfGuests();
        savedOrderTable.acceptGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }

    private void changeTableStatus(final boolean empty, final OrderTable savedOrderTable) {
        if (empty) {
            savedOrderTable.clear();
            return;
        }
        savedOrderTable.acceptGuests();
    }
}

package kitchenpos.application.ordertable;

import javax.transaction.Transactional;
import kitchenpos.application.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.ordertable.dto.OrderTableChangeGuestRequest;
import kitchenpos.application.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.application.ordertable.dto.OrderTableResponse;
import kitchenpos.application.ordertable.dto.OrderTablesResponse;
import kitchenpos.domain.orertable.OrderTable;
import kitchenpos.domain.orertable.OrderTableRepository;
import kitchenpos.domain.orertable.OrderTableValidator;
import kitchenpos.domain.orertable.exception.OrderTableException;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderTableService(final OrderTableRepository orderTableRepository,
                             final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTablesResponse list() {
        return OrderTablesResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableException.NotFoundOrderTableException::new);
        orderTableValidator.validateChangeEmpty(orderTableId, savedOrderTable);
        savedOrderTable.changeEmptyStatus(request.getEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeGuestRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableException.NotFoundOrderTableException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }
}

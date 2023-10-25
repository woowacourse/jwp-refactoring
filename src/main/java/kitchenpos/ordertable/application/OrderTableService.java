package kitchenpos.ordertable.application;

import javax.transaction.Transactional;
import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeGuestRequest;
import kitchenpos.ordertable.application.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.application.dto.OrderTableResponse;
import kitchenpos.ordertable.application.dto.OrderTablesResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.exception.OrderTableException;
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

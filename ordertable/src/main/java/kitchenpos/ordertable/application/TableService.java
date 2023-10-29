package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.validator.OrderTableValidator;
import kitchenpos.ordertable.application.request.OrderTableRequest;
import kitchenpos.ordertable.application.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ordertable.application.request.UpdateOrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.application.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(
                orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty()
        );
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final UpdateOrderTableEmptyRequest updateOrderTableEmptyRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.setEmpty(updateOrderTableEmptyRequest.isEmpty(), orderTableValidator);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final UpdateOrderTableNumberOfGuestsRequest updateOrderTableNumberOfGuestsRequest) {
        final int numberOfGuests = updateOrderTableNumberOfGuestsRequest.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> allOrderTable = orderTableRepository.findAll();
        return allOrderTable.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }
}

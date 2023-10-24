package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.validator.OrderTableValidator;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.request.UpdateOrderTableNumberOfGuestsRequest;
import kitchenpos.ui.response.OrderTableResponse;
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
        orderTableValidator.validateCompletedOrderTable(orderTable);
        orderTable.setEmpty(updateOrderTableEmptyRequest.isEmpty());
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
        final List<OrderTable> all = orderTableRepository.findAll();
        return all.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }
}

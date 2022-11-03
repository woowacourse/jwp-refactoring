package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderCompletionValidator;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderCompletionValidator orderCompletionValidator;

    public TableService(OrderTableRepository orderTableRepository, OrderCompletionValidator orderCompletionValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderCompletionValidator = orderCompletionValidator;
    }

    @Transactional
    public OrderTable create(int numberOfGuest, boolean empty) {
        return orderTableRepository.save(new OrderTable(numberOfGuest, empty));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        var orderTable = orderTableRepository.getById(orderTableId);

        orderTable.changeEmpty(request.isEmpty(), orderCompletionValidator);
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        var orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuest(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }
}

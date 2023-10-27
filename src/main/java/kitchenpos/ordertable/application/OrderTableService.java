package kitchenpos.ordertable.application;

import kitchenpos.ordertable.Empty;
import kitchenpos.ordertable.NumberOfGuests;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import kitchenpos.ordertable.OrderTableValidator;
import kitchenpos.ordertable.application.request.ChangeEmptyRequest;
import kitchenpos.ordertable.application.request.NumberOfGuestsRequest;
import kitchenpos.ordertable.application.request.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderTableService(
            final OrderTableRepository orderTableRepository,
            final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public OrderTable create(final OrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(request.getNumberOfGuests()), Empty.from(request.isEmpty()));

        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = validateOrderTable(orderTableId);

        savedOrderTable.updateEmpty(Empty.from(request.isEmpty()));

        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
        orderTableValidator.validate(orderTableId, Arrays.asList("COOKING", "MEAL"));

        return orderTable;
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateNumberOfGuests(new NumberOfGuests(numberOfGuests));

        return orderTableRepository.save(savedOrderTable);
    }
}

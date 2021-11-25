package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.validate(tableValidator);
        savedOrderTable.changeEmptyStatus(empty);
        return savedOrderTable;
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }
}

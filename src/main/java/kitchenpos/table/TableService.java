package kitchenpos.table;

import kitchenpos.table.validator.TableChangeEmptyValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableChangeEmptyValidator tableChangeEmptyValidator;

    public TableService(
            OrderTableRepository orderTableRepository,
            TableChangeEmptyValidator tableChangeEmptyValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableChangeEmptyValidator = tableChangeEmptyValidator;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        tableChangeEmptyValidator.validate(orderTableId);
        savedOrderTable.changeEmpty(empty);

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }
}

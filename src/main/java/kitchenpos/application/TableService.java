package kitchenpos.application;

import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.TableValidator;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(TableValidator tableValidator, OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(null, orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateTableGroupForChangeEmpty();

        tableValidator.validateUnGroupCondition(savedOrderTable.getId());

        return orderTableRepository.save(new OrderTable(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(), orderTableRequest.isEmpty()));
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateEmptyForChangeGuestNumber();

        return orderTableRepository.save(new OrderTable(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                orderTableRequest.getNumberOfGuests(), savedOrderTable.isEmpty()));
    }
}

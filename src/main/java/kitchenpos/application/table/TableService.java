package kitchenpos.application.table;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import kitchenpos.application.tablegroup.GroupingEvent;
import kitchenpos.application.tablegroup.UngroupingEvent;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.ChangeNumberOfGuestRequest;
import kitchenpos.dto.OrderTableRequest;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        final OrderTable orderTable = request.toOrderTable();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.updateEmptyStatus(request.isEmpty(), tableValidator);
        return savedOrderTable;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void group(final GroupingEvent groupingEvent) {
        final List<Long> ids = groupingEvent.getTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllById(ids);

        if (orderTables.size() != ids.size()) {
            throw new IllegalArgumentException("유효하지 않은 OrderTable을 포함하고 있습니다.");
        }

        orderTables.forEach(orderTable -> orderTable.group(groupingEvent.getTableGroupId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void ungroup(final UngroupingEvent ungroupingEvent) {
        final List<OrderTable> tables = orderTableRepository.findAllByGroupId(ungroupingEvent.getTableGroupId());
        tables.forEach(table -> table.ungroup(tableValidator));
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("없는 테이블이에요"));
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.updateNumberOfGuest(request.getNumberOfGuest());

        return savedOrderTable;
    }
}

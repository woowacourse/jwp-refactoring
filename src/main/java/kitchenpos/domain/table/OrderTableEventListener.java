package kitchenpos.domain.table;

import kitchenpos.domain.tablegroup.TableGroupCreatedEvent;
import kitchenpos.domain.tablegroup.TableUngroupedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderTableEventListener {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTableGroup(TableGroupCreatedEvent event) {
        for (Long orderTableId : event.getOrderTableIds()) {
            OrderTable orderTable = orderTableRepository.findById(orderTableId)
                    .orElseThrow(IllegalArgumentException::new);
            orderTableRepository.save(orderTable.group(event.getTableGroupId()));
        }
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTableUnGroup(TableUngroupedEvent event) {
        for (OrderTable orderTable : orderTableRepository.findAllByTableGroupId(event.getTableGroupId())) {
            orderTableRepository.save(orderTable.ungroup());
        }
    }
}

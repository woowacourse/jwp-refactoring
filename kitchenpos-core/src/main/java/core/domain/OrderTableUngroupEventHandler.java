package core.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
public class OrderTableUngroupEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public OrderTableUngroupEventHandler(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(UnGroupEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup(tableValidator);
            orderTableRepository.save(orderTable);
        }
    }
}

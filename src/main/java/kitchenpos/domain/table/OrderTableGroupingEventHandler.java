package kitchenpos.domain.table;

import kitchenpos.domain.tablegroup.GroupingEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
public class OrderTableGroupingEventHandler {
    private OrderTableRepository orderTableRepository;

    public OrderTableGroupingEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(GroupingEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(event.getTableGroupId());
            orderTableRepository.save(orderTable);
        }
    }
}

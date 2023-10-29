package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.GroupTableEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GroupTableEventListener {

    private final OrderTableRepository orderTableRepository;

    public GroupTableEventListener(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(final GroupTableEvent groupTableEvent) {
        for (final OrderTable orderTable : getValidatedOrderTables(groupTableEvent)) {
            orderTable.groupBy(groupTableEvent.getTableGroup().getId());
        }
    }

    private List<OrderTable> getValidatedOrderTables(final GroupTableEvent groupTableEvent) {
        final List<Long> orderTableIds = groupTableEvent.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("그룹화를 요청한 테이블 중에 존재하지 않는 테이블이 포함되어 있습니다.");
        }

        return savedOrderTables;
    }
}

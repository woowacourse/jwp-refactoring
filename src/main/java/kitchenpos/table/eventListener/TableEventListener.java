package kitchenpos.table.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.event.CheckOrderableTableEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class TableEventListener {

    private final OrderTableRepository orderTableRepository;

    public TableEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void checkOrderableTable(CheckOrderableTableEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }
}

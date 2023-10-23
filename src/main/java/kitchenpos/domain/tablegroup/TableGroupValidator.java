package kitchenpos.domain.tablegroup;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupValidator {

    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateGroup(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블의 개수와 맞지 않습니다");
        }
    }
}

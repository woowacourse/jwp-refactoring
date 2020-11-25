package kitchenpos.application.verifier;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;
import kitchenpos.domain.model.tablegroup.TableGroup;

@Component
public class CreateTableGroupVerifier {
    private final OrderTableRepository orderTableRepository;

    public CreateTableGroupVerifier(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroup toTableGroup(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        if (savedOrderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException();
        }

        return new TableGroup(null, savedOrderTables, null);
    }
}

package kitchenpos.domain.model.tablegroup;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.domain.model.AggregateReference;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

@Service
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

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        return new TableGroup(null, toOrderTables(orderTableIds), null);
    }

    private List<AggregateReference<OrderTable>> toOrderTables(List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(AggregateReference<OrderTable>::new)
                .collect(toList());
    }
}

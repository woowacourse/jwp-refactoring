package kitchenpos.domain.service;

import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;

@Service
public class TableGroupCreateService {
    private final OrderTableRepository orderTableRepository;

    public TableGroupCreateService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }
}

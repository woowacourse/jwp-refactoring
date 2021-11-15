package kitchenpos.table.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableCondition orderTableCondition;

    public TableService(OrderTableRepository orderTableRepository,
                        OrderTableCondition orderTableCondition) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableCondition = orderTableCondition;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.fromList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderTableCondition.isUnableToChange(orderTableId)) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(isEmpty);

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> findAllByTableGroupId(Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository
            .findAllByTableGroup(tableGroupId);

        return OrderTableResponse.fromList(orderTables);
    }
}

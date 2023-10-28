package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private static final int MIN_NUMBER_OF_ORDER_TABLE = 2;

    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final List<Long> orderTableIds) {
        validateOrderTableSize(orderTableIds);
        final List<OrderTable> savedOrderTables = findOrderTablesByIds(orderTableIds);
        validateValidOrderTablesSize(orderTableIds, savedOrderTables);
        validateOrderTablesCanGroup(savedOrderTables);
    }

    private void validateOrderTableSize(final List<Long> orderTableIds) {
        if (orderTableIds.size() < MIN_NUMBER_OF_ORDER_TABLE) {
            throw new IllegalArgumentException("2개 이상의 주문 테이블을 그룹으로 만들 수 있습니다.");
        }
    }

    private List<OrderTable> findOrderTablesByIds(final List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void validateValidOrderTablesSize(final List<Long> orderTableIds,
                                              final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블의 정보가 올바르지 않습니다.");
        }
    }

    private void validateOrderTablesCanGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateEmpty(orderTable);
            validateGroupedAlready(orderTable);
        }
    }

    private void validateEmpty(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않습니다.");
        }
    }

    private void validateGroupedAlready(final OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("이미 테이블 그룹으로 등록되어 있습니다.");
        }
    }
}

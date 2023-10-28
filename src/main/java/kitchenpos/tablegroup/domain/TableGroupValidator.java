package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final List<Long> orderTableIds) {
        validateTableIdSize(orderTableIds);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateSameSize(orderTableIds, savedOrderTables);

        makeTableGroup(savedOrderTables);
    }

    private void validateTableIdSize(final List<Long> orderTableIds) {
        if (orderTableIds.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }
    }

    private void validateSameSize(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("요청한 주문 테이블 수와 저장된 주문 테이블의 수가 다릅니다.");
        }
    }

    private void makeTableGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateEmptyOrderTable(orderTable);
            orderTable.validateNoTableGroup();

            orderTable.changeEmpty(false);
        }
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블은 비어있어야 합니다.");
        }
    }
}

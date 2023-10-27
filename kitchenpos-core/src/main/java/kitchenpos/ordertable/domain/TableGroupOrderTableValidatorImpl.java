package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupOrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class TableGroupOrderTableValidatorImpl implements TableGroupOrderTableValidator {

    private final OrderTableRepository orderTableRepository;

    public TableGroupOrderTableValidatorImpl(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateOrderTablesByIds(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹을 만들기 위해선, 적어도 2개 이상의 주문 테이블이 필요합니다.");
        }

        validateOrderTables(orderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        orderTables.forEach(this::validateOrderTable);
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 빈 상태여야 합니다.");
        }
        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("주문 테이블이 이미 다른 그룹에 속해있습니다.");
        }
    }
}

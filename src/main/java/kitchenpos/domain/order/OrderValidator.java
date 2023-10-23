package kitchenpos.domain.order;

import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.dao.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateExistTable(final Long tableId) {
        final OrderTable orderTable = orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        validateOrderTableIsNotEmpty(orderTable);
    }

    private void validateOrderTableIsNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("Order from empty table is not allowed");
        }
    }
}

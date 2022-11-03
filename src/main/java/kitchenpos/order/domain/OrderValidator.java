package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(final Order order, final List<Long> menuIds) {
        validateOrderLineItemMatchMenu(order, menuIds);
        validateOrderTableIsOrderable(order);
    }

    private void validateOrderLineItemMatchMenu(final Order order, final List<Long> menuIds) {
        order.validateOrderLineItemSize(menuRepository.countByIdIn(menuIds));
    }

    private void validateOrderTableIsOrderable(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(NotFoundOrderTableException::new);
        orderTable.validateOrderable();
    }
}

package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.dao.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuDao menuDao;

    private final OrderTableDao orderTableDao;

    public OrderValidator(MenuDao menuDao, OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
    }

    public void validate(final Order order, final List<Long> menuIds) {
        validateOrderLineItemMatchMenu(order, menuIds);
        validateIsExistOrderTable(order.getOrderTableId());
    }

    private void validateOrderLineItemMatchMenu(final Order order, final List<Long> menuIds) {
        order.validateOrderLineItemMatchMenu(menuDao.countByIdIn(menuIds));
    }

    private void validateIsExistOrderTable(Long orderTableId) {
        orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderTableDao;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableDao orderTableDao;
    private final MenuDao menuDao;

    public OrderValidatorImpl(final OrderTableDao orderTableDao, final MenuDao menuDao) {
        this.orderTableDao = orderTableDao;
        this.menuDao = menuDao;
    }

    @Override
    public void validate(final Order order) {
        final OrderTable orderTable = orderTableDao.getById(order.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);
        validateMenusExist(order);
    }

    private void validateMenusExist(final Order order) {
        List<Long> menuIds = order.getOrderLineItems().stream()
            .map(OrderLineItem::getOrderMenuId)
            .collect(Collectors.toUnmodifiableList());

        if (menuDao.countByIdIn(menuIds) != menuIds.size()) {
            throw new IllegalArgumentException("menus are not completely found");
        }
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}

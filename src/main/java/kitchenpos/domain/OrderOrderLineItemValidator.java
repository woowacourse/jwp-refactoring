package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderOrderLineItemValidator implements OrderValidator {

    private final MenuDao menuDao;

    public OrderOrderLineItemValidator(final MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public void validate(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        validateEmptinessOf(orderLineItems);

        if (hasInvalidMenuId(getMenuIds(orderLineItems))) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateEmptinessOf(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private static List<Long> getMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems
                .stream().map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private boolean hasInvalidMenuId(final List<Long> menuIds) {
        return menuIds.size() != menuDao.countByIdIn(menuIds);
    }
}

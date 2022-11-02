package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemValidator {

    private final MenuDao menuDao;

    public OrderLineItemValidator(final MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = getMenuIds(orderLineItems);
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
    }

    private List<Long> getMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }
}

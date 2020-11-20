package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderVerifier {
    private final MenuDao menuDao;

    public OrderVerifier(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public Order toOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderTable) || orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        return new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
    }
}

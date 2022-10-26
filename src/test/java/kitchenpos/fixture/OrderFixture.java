package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture extends DomainCreator {

    public static Order createRequestOrderStatus(OrderStatus orderStatus) {
        return createOrder(null, null, orderStatus.name(), LocalDateTime.now(), new ArrayList<>());
    }

    public static OrderLineItem createOrderLineItem(Long id, Long menuId) {
        return createOrderLineItem(id, null, menuId, 1);
    }
}

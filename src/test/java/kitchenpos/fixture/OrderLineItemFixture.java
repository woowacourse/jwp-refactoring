package kitchenpos.fixture;

import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem orderLineItem(Menu menu, Order order, long quantity) {
        return new OrderLineItem(menu, order, quantity);
    }

    public static OrderRequest.OrderLineItemRequest orderLineItemRequest(Long menuId, long quantity) {
        return new OrderRequest.OrderLineItemRequest(menuId, quantity);
    }
}

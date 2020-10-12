package kitchenpos.integrationtest.step;

import java.util.Collections;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderServiceTestStep {
	public static Order createValidOrder() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setQuantity(1);

		Order order = new Order();
		order.setOrderTableId(2L);
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		return order;
	}
}

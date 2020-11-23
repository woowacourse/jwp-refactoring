package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
	@OneToMany(mappedBy = "orderId", fetch = FetchType.EAGER)
	private List<OrderLineItem> orderLineItems;

	protected OrderLineItems() {
	}

	public OrderLineItems(List<OrderLineItem> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public List<Long> extractMenuIds() {
		return orderLineItems.stream()
			.map(OrderLineItem::getMenuId)
			.collect(Collectors.toList());
	}

	public boolean isEmpty() {
		return orderLineItems.isEmpty();
	}

	public boolean hasNotSize(long size) {
		return orderLineItems.size() != size;
	}

	public void associateOrder(long orderId) {
		for (OrderLineItem orderLineItem : orderLineItems) {
			orderLineItem.setOrderId(orderId);
		}
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}
}

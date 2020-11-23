package kitchenpos.dto.tableGroup;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTables;

public class OrderTableCreateResponses {
	private List<OrderTableCreateResponse> orderTableCreateResponses;

	protected OrderTableCreateResponses() {
	}

	public OrderTableCreateResponses(
		List<OrderTableCreateResponse> orderTableCreateResponses) {
		this.orderTableCreateResponses = orderTableCreateResponses;
	}

	public static OrderTableCreateResponses from(OrderTables orderTables) {
		return orderTables.getOrderTables()
			.stream()
			.map(OrderTableCreateResponse::new)
			.collect(Collectors.collectingAndThen(Collectors.toList(), OrderTableCreateResponses::new));
	}

	public List<OrderTableCreateResponse> getOrderTableCreateResponses() {
		return orderTableCreateResponses;
	}
}

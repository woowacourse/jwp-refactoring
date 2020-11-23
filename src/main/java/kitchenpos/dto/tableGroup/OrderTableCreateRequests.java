package kitchenpos.dto.tableGroup;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTables;

public class OrderTableCreateRequests {
	private List<OrderTableCreateRequest> orderTableCreateRequests;

	protected OrderTableCreateRequests() {
	}

	public OrderTableCreateRequests(
		List<OrderTableCreateRequest> orderTableCreateRequests) {
		this.orderTableCreateRequests = orderTableCreateRequests;
	}

	public static OrderTableCreateRequests from(OrderTables orderTables) {
		return orderTables.getOrderTables()
			.stream()
			.map(OrderTableCreateRequest::new)
			.collect(Collectors.collectingAndThen(Collectors.toList(), OrderTableCreateRequests::new));
	}

	public List<OrderTableCreateRequest> getOrderTableCreateRequests() {
		return orderTableCreateRequests;
	}

	public OrderTables toOrderTables() {
		return orderTableCreateRequests.stream()
			.map(OrderTableCreateRequest::toEntity)
			.collect(Collectors.collectingAndThen(Collectors.toList(), OrderTables::new));
	}
}

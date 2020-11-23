package kitchenpos.dto.table;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;

public class OrderTableFindAllResponses {
	private List<OrderTableFindAllResponse> orderTableFindAllResponses;

	protected OrderTableFindAllResponses() {
	}

	public OrderTableFindAllResponses(
		List<OrderTableFindAllResponse> orderTableFindAllResponses) {
		this.orderTableFindAllResponses = orderTableFindAllResponses;
	}

	public static OrderTableFindAllResponses from(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableFindAllResponse::new)
			.collect(Collectors.collectingAndThen(Collectors.toList(), OrderTableFindAllResponses::new));
	}

	public List<OrderTableFindAllResponse> getOrderTableFindAllResponses() {
		return orderTableFindAllResponses;
	}
}

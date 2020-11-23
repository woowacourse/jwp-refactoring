package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
	@OneToMany(mappedBy = "tableGroupId", fetch = FetchType.EAGER)
	private List<OrderTable> orderTables;

	protected OrderTables() {
	}

	public OrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public int size() {
		return orderTables.size();
	}

	public List<Long> extractOrderTableIds() {
		return orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());
	}

	public void setTableGroupId(Long tableGroupId) {
		for (OrderTable orderTable : orderTables) {
			orderTable.setTableGroupId(tableGroupId);
		}
	}

	public void setEmpty(boolean empty) {
		for (OrderTable orderTable : orderTables) {
			orderTable.setEmpty(empty);
		}
	}
}

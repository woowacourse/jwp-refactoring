package kitchenpos.dto.table;

public class OrderTableUpdateEmptyRequest {
	private Boolean empty;

	protected OrderTableUpdateEmptyRequest() {
	}

	public OrderTableUpdateEmptyRequest(Boolean empty) {
		this.empty = empty;
	}

	public Boolean getEmpty() {
		return empty;
	}
}

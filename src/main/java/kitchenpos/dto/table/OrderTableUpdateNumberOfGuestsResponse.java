package kitchenpos.dto.table;

import kitchenpos.domain.OrderTable;

public class OrderTableUpdateNumberOfGuestsResponse {
	private Long id;
	private Long tableGroupId;
	private Integer numberOfGuests;
	private Boolean empty;

	protected OrderTableUpdateNumberOfGuestsResponse() {
	}

	public OrderTableUpdateNumberOfGuestsResponse(Long id, Long tableGroupId, Integer numberOfGuests,
		Boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTableUpdateNumberOfGuestsResponse(OrderTable orderTable) {
		this(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean getEmpty() {
		return empty;
	}
}

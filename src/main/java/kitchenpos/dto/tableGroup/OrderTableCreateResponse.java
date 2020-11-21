package kitchenpos.dto.tableGroup;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateResponse {
	private Long id;
	private Long tableGroupId;
	private Integer numberOfGuests;
	private Boolean empty;

	protected OrderTableCreateResponse() {
	}

	public OrderTableCreateResponse(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTableCreateResponse(OrderTable orderTable) {
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

package kitchenpos.dto.tableGroup;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {
	private Long id;
	private Long tableGroupId;
	private Integer numberOfGuests;
	private Boolean empty;

	protected OrderTableCreateRequest() {
	}

	public OrderTableCreateRequest(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTableCreateRequest(OrderTable orderTable) {
		this(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
	}

	public OrderTable toEntity() {
		return new OrderTable(id, tableGroupId, numberOfGuests, empty);
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

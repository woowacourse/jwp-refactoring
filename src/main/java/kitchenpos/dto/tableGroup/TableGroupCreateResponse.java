package kitchenpos.dto.tableGroup;

import java.time.LocalDateTime;

import kitchenpos.domain.TableGroup;

public class TableGroupCreateResponse {
	private Long id;
	private LocalDateTime createdDate;
	private OrderTableCreateResponses orderTables;

	protected TableGroupCreateResponse() {
	}

	public TableGroupCreateResponse(Long id, LocalDateTime createdDate,
		OrderTableCreateResponses orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public TableGroupCreateResponse(TableGroup tableGroup) {
		this(tableGroup.getId(), tableGroup.getCreatedDate(),
			OrderTableCreateResponses.from(tableGroup.getOrderTables()));
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public OrderTableCreateResponses getOrderTables() {
		return orderTables;
	}
}

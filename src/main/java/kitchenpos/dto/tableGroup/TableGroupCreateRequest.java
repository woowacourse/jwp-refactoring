package kitchenpos.dto.tableGroup;

import java.time.LocalDateTime;

import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {
	private Long id;
	private LocalDateTime createdDate;
	private OrderTableCreateRequests orderTables;

	protected TableGroupCreateRequest() {
	}

	public TableGroupCreateRequest(Long id, LocalDateTime createdDate,
		OrderTableCreateRequests orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public TableGroup toEntity() {
		return new TableGroup(
			this.id,
			this.createdDate,
			orderTables.toOrderTables()
		);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public OrderTableCreateRequests getOrderTables() {
		return orderTables;
	}
}

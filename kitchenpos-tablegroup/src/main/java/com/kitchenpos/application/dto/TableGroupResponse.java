package com.kitchenpos.application.dto;

import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.TableGroup;
import com.kitchenpos.ui.dto.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses;

    private TableGroupResponse() {
    }

    private TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse from(final TableGroup tableGroup, List<OrderTable> orderTables) {
        List<OrderTableResponse> orderTablesResponse = orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTablesResponse
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}

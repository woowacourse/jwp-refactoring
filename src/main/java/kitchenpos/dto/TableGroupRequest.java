package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private List<OrderTableId> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds.stream()
            .map(OrderTableId::new)
            .collect(Collectors.toList());
    }

    public List<OrderTableId> getOrderTables() {
        return orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds.stream()
            .map(OrderTableId::getId)
            .collect(Collectors.toList());
    }

    public TableGroup toEntity(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

    public static class OrderTableId {
        private Long id;

        public OrderTableId() {
        }

        public OrderTableId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}

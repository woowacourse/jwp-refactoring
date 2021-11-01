package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    protected TableGroupCreateRequest() {
    }

    public TableGroup toEntity() {
        return new TableGroup(
                orderTables.stream()
                        .map(table -> new OrderTable(table.id))
                        .collect(Collectors.toList())
        );
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableRequest {

        private long id;

        protected OrderTableRequest() {
        }

        public long getId() {
            return id;
        }
    }
}
